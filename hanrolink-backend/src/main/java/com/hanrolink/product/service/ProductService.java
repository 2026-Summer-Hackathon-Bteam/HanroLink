package com.hanrolink.product.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.enums.AccountRole;
import com.hanrolink.account.enums.BusinessUserAccountRole;
import com.hanrolink.account.enums.JwtAccountRole;
import com.hanrolink.account.exception.UnsupportedJwtAccountRoleException;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.account.repository.projection.AuthenticatedBusinessUserAccountProjection;
import com.hanrolink.negotiationrequest.policy.NegotiationRequestPolicy;
import com.hanrolink.negotiationrequest.repository.ProductNegotiationRequestRepository;
import com.hanrolink.product.policy.MonthlySupplyCapacityPolicy;
import com.hanrolink.product.repository.MonthlySupplyCapacityRepository;
import com.hanrolink.product.repository.ProductRepository;
import com.hanrolink.product.repository.ProductStoryRepository;
import com.hanrolink.product.repository.projection.ProductDetailProjection;
import com.hanrolink.product.response.ProductDetailResponse;
import com.hanrolink.product.response.component.MonthlySupplyCapacityResponse;
import com.hanrolink.product.response.component.ProductExpirationTypeResponse;
import com.hanrolink.product.response.component.ProductMainIngredientRegionResponse;
import com.hanrolink.product.response.component.ProductPermissionsResponse;
import com.hanrolink.product.response.component.ProductStoryResponse;
import com.hanrolink.product.response.component.ProductSupplierResponse;
import com.hanrolink.product.response.component.StorageTypeResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryResponse;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  private final MonthlySupplyCapacityRepository monthlySupplyCapacityRepository;

  private final ProductStoryRepository productStoryRepository;

  private final ProductNegotiationRequestRepository productNegotiationRequestRepository;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  public ProductService(
    ProductRepository productRepository,
    MonthlySupplyCapacityRepository monthlySupplyCapacityRepository,
    ProductStoryRepository productStoryRepository,
    ProductNegotiationRequestRepository productNegotiationRequestRepository,
    BusinessUserAccountRepository businessUserAccountRepository
  ) {
    this.productRepository = productRepository;
    this.monthlySupplyCapacityRepository = monthlySupplyCapacityRepository;
    this.productStoryRepository = productStoryRepository;
    this.productNegotiationRequestRepository = productNegotiationRequestRepository;
    this.businessUserAccountRepository = businessUserAccountRepository;
  }

  /**
   * 商品詳細情報を取得する
   * @param authenticatedJwtAccountRole JWTから取得したアカウントロール
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param productId 取得対象の商品ID
   * @return 商品詳細情報
   */
  @Transactional(readOnly = true)
  public ProductDetailResponse getDetail(
    JwtAccountRole authenticatedJwtAccountRole,
    String identityProviderSubject,
    Long productId
  ) {
    // 認証情報に基づく商品詳細の閲覧者情報の取得
    ProductDetailViewer authenticatedAccount = resolveViewer(
      authenticatedJwtAccountRole,
      identityProviderSubject
    );

    // 商品詳細の表示に必要な基本情報と関連情報の取得
    ProductDetailProjection product = productRepository
      .findDetailById(
        productId,
        authenticatedAccount.id()
      )
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    List<MonthlySupplyCapacityResponse> monthlySupplyCapacities =
      monthlySupplyCapacityRepository
        .findListByProductId(
          productId,
          Pageable.ofSize(MonthlySupplyCapacityPolicy.TARGET_MONTH_COUNT)
        );

    List<ProductStoryResponse> productStories = productStoryRepository
      .findListByProductId(productId)
      .stream()
      .map(productStory ->
        new ProductStoryResponse(
          productStory.id(),
          productStory.productStorySectionTemplateId(),
          productStory.position(),
          productStory.sectionTitle(),
          productStory.body(),
          // TODO: S3連携時にストレージキーを署名付きURLへ変換する
          "dummy/" + productStory.imageStorageKey()
        )
      )
      .toList();

    // 閲覧者に応じた操作権限と商談申請状態の判定
    boolean canManage =
      authenticatedAccount.id() != null
        && authenticatedAccount.id().equals(product.supplierAccountId());

    boolean canCreateNegotiationRequest =
      authenticatedAccount.role() == AccountRole.BUYER;

    boolean hasMyActiveNegotiationRequest = false;

    if (canCreateNegotiationRequest) {
      Instant activeSince =
        Instant.now().minus(
          NegotiationRequestPolicy.ACTIVE_PERIOD_DAYS,
          ChronoUnit.DAYS
        );

      hasMyActiveNegotiationRequest =
        productNegotiationRequestRepository
          .existsActiveByProductIdAndBuyerAccountId(
            productId,
            authenticatedAccount.id(),
            activeSince
          );
    }

    return new ProductDetailResponse(
      product.id(),
      product.name(),
      product.hiddenAt() != null,
      new ProductCategoryResponse(
        product.productCategoryId(),
        product.productCategoryName()
      ),
      new ProductMainIngredientRegionResponse(
        product.mainIngredientRegionId(),
        product.mainIngredientRegionName()
      ),
      product.contentQuantity(),
      new ProductExpirationTypeResponse(
        product.expirationType(),
        product.expirationType().getDisplayName()
      ),
      product.shelfLifeDays(),
      new StorageTypeResponse(
        product.storageType(),
        product.storageType().getDisplayName()
      ),
      product.desiredRetailPrice(),
      product.allergyInformation(),
      product.certificationInformation(),
      product.caseSize(),
      product.unitsPerCase(),
      product.minimumOrderQuantity(),
      product.shippingLeadTimeDays(),
      product.salesAreaRestriction(),
      // TODO: S3連携時にストレージキーを署名付きURLへ変換する
      "dummy/" + product.mainImageStorageKey(),
      monthlySupplyCapacities,
      productStories,
      new ProductSupplierResponse(
        product.supplierBusinessName(),
        product.supplierBusinessAddressPrefecture(),
        product.supplierBusinessAddressMunicipalityStreet(),
        product.supplierBusinessAddressBuilding(),
        product.supplierBusinessWebsiteUrl()
      ),
      new ProductPermissionsResponse(
        canManage,
        canCreateNegotiationRequest
      ),
      hasMyActiveNegotiationRequest
    );
  }

  private ProductDetailViewer resolveViewer(
    JwtAccountRole authenticatedJwtAccountRole,
    String identityProviderSubject
  ) {
    if (authenticatedJwtAccountRole == JwtAccountRole.ADMIN) {
      return new ProductDetailViewer(
        null,
        AccountRole.ADMIN
      );
    }

    if (authenticatedJwtAccountRole != null) {
      throw new UnsupportedJwtAccountRoleException();
    }

    AuthenticatedBusinessUserAccountProjection account =
      businessUserAccountRepository
        .findAuthenticatedAccountByIdentityProviderSubject(
          identityProviderSubject
        )
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return new ProductDetailViewer(
      account.id(),
      accountRoleOf(account.role())
    );
  }

  private AccountRole accountRoleOf(
    BusinessUserAccountRole role
  ) {
    return switch (role) {
      case SUPPLIER -> AccountRole.SUPPLIER;
      case BUYER -> AccountRole.BUYER;
    };
  }

  private record ProductDetailViewer(
    Long id,
    AccountRole role
  ) {}
}
