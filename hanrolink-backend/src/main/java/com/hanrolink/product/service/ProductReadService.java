package com.hanrolink.product.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.exception.UnsupportedJwtAccountRoleException;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.account.repository.projection.BusinessUserAccountAccessScopeProjection;
import com.hanrolink.business.enums.BusinessRole;
import com.hanrolink.infrastructure.s3.S3DownloadUrlGenerator;
import com.hanrolink.negotiationrequest.policy.NegotiationRequestPolicy;
import com.hanrolink.negotiationrequest.policy.ProductNegotiationRequestPolicy;
import com.hanrolink.negotiationrequest.repository.ProductNegotiationRequestRepository;
import com.hanrolink.pagination.response.component.PaginationResponse;
import com.hanrolink.product.policy.MonthlySupplyCapacityPolicy;
import com.hanrolink.product.repository.MonthlySupplyCapacityRepository;
import com.hanrolink.product.repository.ProductRepository;
import com.hanrolink.product.repository.ProductStoryRepository;
import com.hanrolink.product.repository.projection.ProductDetailProjection;
import com.hanrolink.product.repository.projection.ProductSearchMonthlySupplyCapacityProjection;
import com.hanrolink.product.repository.projection.ProductSearchResultProjection;
import com.hanrolink.product.request.ProductSearchRequest;
import com.hanrolink.product.response.ProductDetailResponse;
import com.hanrolink.product.response.ProductSearchResponse;
import com.hanrolink.product.response.component.MonthlySupplyCapacityResponse;
import com.hanrolink.product.response.component.ProductExpirationTypeResponse;
import com.hanrolink.product.response.component.ProductMainIngredientRegionResponse;
import com.hanrolink.product.response.component.ProductPermissionsResponse;
import com.hanrolink.product.response.component.ProductSearchResultResponse;
import com.hanrolink.product.response.component.ProductStoryResponse;
import com.hanrolink.product.response.component.ProductSupplierResponse;
import com.hanrolink.product.response.component.StorageTypeResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryResponse;
import com.hanrolink.security.authorization.enums.ApplicationRole;
import com.hanrolink.security.authorization.enums.JwtAccountRole;

@Profile("s3")
@Service
public class ProductReadService {

  private final ProductRepository productRepository;

  private final MonthlySupplyCapacityRepository monthlySupplyCapacityRepository;

  private final ProductStoryRepository productStoryRepository;

  private final ProductNegotiationRequestRepository productNegotiationRequestRepository;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  private final S3DownloadUrlGenerator s3DownloadUrlGenerator;

  public ProductReadService(
    ProductRepository productRepository,
    MonthlySupplyCapacityRepository monthlySupplyCapacityRepository,
    ProductStoryRepository productStoryRepository,
    ProductNegotiationRequestRepository productNegotiationRequestRepository,
    BusinessUserAccountRepository businessUserAccountRepository,
    S3DownloadUrlGenerator s3DownloadUrlGenerator
  ) {
    this.productRepository = productRepository;
    this.monthlySupplyCapacityRepository = monthlySupplyCapacityRepository;
    this.productStoryRepository = productStoryRepository;
    this.productNegotiationRequestRepository = productNegotiationRequestRepository;
    this.businessUserAccountRepository = businessUserAccountRepository;
    this.s3DownloadUrlGenerator = s3DownloadUrlGenerator;
  }

  /**
   * 商品詳細情報を取得する
   * @param authenticatedJwtAccountRole JWTから取得したアカウントロール
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param productPublicId 取得対象の商品の公開識別子
   * @return 商品詳細情報
   */
  @Transactional(readOnly = true)
  public ProductDetailResponse getDetail(
    JwtAccountRole authenticatedJwtAccountRole,
    String identityProviderSubject,
    UUID productPublicId
  ) {
    // 認証情報に基づく商品詳細の閲覧者情報の取得
    ProductDetailViewer viewer = resolveViewer(
      authenticatedJwtAccountRole,
      identityProviderSubject
    );

    // 商品詳細の表示に必要な基本情報と関連情報の取得
    ProductDetailProjection product = productRepository
      .findDetailByPublicId(
        productPublicId,
        viewer.businessId()
      )
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    List<MonthlySupplyCapacityResponse> monthlySupplyCapacities =
      monthlySupplyCapacityRepository
        .findLatestListByProductId(
          product.id(),
          Pageable.ofSize(MonthlySupplyCapacityPolicy.TARGET_MONTH_COUNT)
        )
        .stream()
        .sorted(
          Comparator.comparing(
            monthlySupplyCapacity ->
              monthlySupplyCapacity.targetMonth()
          )
        )
        .map(monthlySupplyCapacity ->
          new MonthlySupplyCapacityResponse(
            YearMonth.from(
              monthlySupplyCapacity.targetMonth()
            ),
            monthlySupplyCapacity.availableQuantity()
          )
        )
        .toList();

    List<ProductStoryResponse> productStories = productStoryRepository
      .findListByProductId(product.id())
      .stream()
      .map(productStory ->
        new ProductStoryResponse(
          productStory.id(),
          productStory.productStorySectionTemplateId(),
          productStory.position(),
          productStory.sectionTitle(),
          productStory.body(),
          s3DownloadUrlGenerator.generate(productStory.imageStorageKey())
        )
      )
      .toList();

    // 閲覧者に応じた操作権限と商談申請状態の判定
    boolean canManage =
      viewer.businessId() != null
        && viewer.businessId().equals(product.supplierBusinessId());

    boolean canCreateNegotiationRequest = false;
    boolean hasMyActiveNegotiationRequest = false;

    if (viewer.role() == ApplicationRole.BUYER) {
      Instant activeSince =
        Instant.now().minus(
          NegotiationRequestPolicy.ACTIVE_PERIOD_DAYS,
          ChronoUnit.DAYS
        );

      long activeNegotiationRequestCount = productNegotiationRequestRepository
        .countActiveByBuyerAccountId(
          viewer.businessUserAccountId(),
          activeSince
        );

      canCreateNegotiationRequest =
        activeNegotiationRequestCount < ProductNegotiationRequestPolicy.MAX_ACTIVE_REQUEST_COUNT;

      hasMyActiveNegotiationRequest =
        productNegotiationRequestRepository
          .existsActiveByProductIdAndBuyerAccountId(
            product.id(),
            viewer.businessUserAccountId(),
            activeSince
          );
    }

    return new ProductDetailResponse(
      product.publicId(),
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
      s3DownloadUrlGenerator.generate(product.mainImageStorageKey()),
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

  /**
   * 指定された条件に基づく商品一覧を取得する
   * @param request 商品の検索条件
   * @return 商品一覧
   */
  @Transactional(readOnly = true)
  public ProductSearchResponse search(
    ProductSearchRequest request
  ) {
    // 検索条件の生成
    List<LocalDate> availableSupplyMonths =
      toMonthStartDates(
        request.availableSupplyMonths()
      );

    Pageable pageable = PageRequest.of(
      request.page() - 1,
      request.pageSize()
    );

    // 条件に一致する商品情報の取得
    Page<ProductSearchResultProjection> productPage =
      productRepository.findSearchResults(
        availableSupplyMonths,
        request.mainIngredientRegionIds(),
        request.productCategoryIds(),
        request.storageTypes(),
        pageable
      );

    // 検索結果に含まれる商品の月別供給可能量の一括取得
    List<Long> productIds =
      productPage
        .getContent()
        .stream()
        .map(product ->
          product.id()
        )
        .toList();

    List<ProductSearchMonthlySupplyCapacityProjection> monthlySupplyCapacities =
      List.of();

    if (!productIds.isEmpty()) {
      monthlySupplyCapacities = monthlySupplyCapacityRepository
        .findSearchResultsByProductIds(productIds);
    }

    // 月別供給可能量の商品IDごとの分類
    Map<Long, List<ProductSearchMonthlySupplyCapacityProjection>>
      monthlySupplyCapacitiesByProductId =
        monthlySupplyCapacities
          .stream()
          .collect(
            Collectors.groupingBy(
              monthlySupplyCapacity ->
                monthlySupplyCapacity.productId()
            )
          );

    // 商品ごとの検索結果レスポンスの生成
    List<ProductSearchResultResponse> products =
      productPage
        .getContent()
        .stream()
        .map(product ->
          new ProductSearchResultResponse(
            product.publicId(),
            product.name(),
            product.businessName(),
            product.productCategoryName(),
            product.mainIngredientRegionName(),
            product.storageType().getDisplayName(),
            toLatestMonthlySupplyCapacityResponses(
              monthlySupplyCapacitiesByProductId
                .getOrDefault(
                  product.id(),
                  List.of()
                )
            ),
            s3DownloadUrlGenerator.generate(product.mainImageStorageKey())
          )
        )
        .toList();

    // ページング情報を含む検索結果全体のレスポンスの生成
    PaginationResponse pagination =
      new PaginationResponse(
        productPage.getNumber() + 1,
        productPage.getSize(),
        productPage.getTotalElements(),
        productPage.getTotalPages()
      );

    return new ProductSearchResponse(
      products,
      pagination
    );
  }

  private ProductDetailViewer resolveViewer(
    JwtAccountRole authenticatedJwtAccountRole,
    String identityProviderSubject
  ) {
    if (authenticatedJwtAccountRole == JwtAccountRole.ADMIN) {
      return new ProductDetailViewer(
        null,
        null,
        ApplicationRole.ADMIN
      );
    }

    if (authenticatedJwtAccountRole != null) {
      throw new UnsupportedJwtAccountRoleException();
    }

    BusinessUserAccountAccessScopeProjection viewerAccessScope =
      businessUserAccountRepository
        .findAccessScopeByIdentityProviderSubject(
          identityProviderSubject
        )
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return new ProductDetailViewer(
      viewerAccessScope.businessUserAccountId(),
      viewerAccessScope.businessId(),
      applicationRoleOf(viewerAccessScope.businessRole())
    );
  }

  private ApplicationRole applicationRoleOf(
    BusinessRole role
  ) {
    return switch (role) {
      case SUPPLIER -> ApplicationRole.SUPPLIER;
      case BUYER -> ApplicationRole.BUYER;
    };
  }

  private record ProductDetailViewer(
    Long businessUserAccountId,
    Long businessId,
    ApplicationRole role
  ) {}

  private List<LocalDate> toMonthStartDates(
    List<YearMonth> months
  ) {
    if (months == null) {
      return List.of();
    }

    return months
      .stream()
      .map(month -> month.atDay(1))
      .toList();
  }

  private List<MonthlySupplyCapacityResponse> toLatestMonthlySupplyCapacityResponses(
    List<ProductSearchMonthlySupplyCapacityProjection> monthlySupplyCapacities
  ) {
    Comparator<ProductSearchMonthlySupplyCapacityProjection> byTargetMonth =
      Comparator.comparing(
        monthlySupplyCapacity ->
          monthlySupplyCapacity.targetMonth()
      );

    return monthlySupplyCapacities
      .stream()
      .sorted(byTargetMonth.reversed())
      .limit(
        MonthlySupplyCapacityPolicy.TARGET_MONTH_COUNT
      )
      .sorted(byTargetMonth)
      .map(monthlySupplyCapacity ->
        new MonthlySupplyCapacityResponse(
          YearMonth.from(
            monthlySupplyCapacity.targetMonth()
          ),
          monthlySupplyCapacity.availableQuantity()
        )
      )
      .toList();
  }
}
