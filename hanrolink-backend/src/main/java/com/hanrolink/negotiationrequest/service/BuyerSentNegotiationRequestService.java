package com.hanrolink.negotiationrequest.service;

import java.time.Instant;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.negotiationrequest.entity.ProductNegotiationRequest;
import com.hanrolink.negotiationrequest.policy.ProductNegotiationRequestPolicy;
import com.hanrolink.negotiationrequest.repository.ProductNegotiationRequestRepository;
import com.hanrolink.negotiationrequest.response.BuyerSentNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProductResponse;
import com.hanrolink.negotiationrequest.snapshot.ProductSnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.MainIngredientOriginPrefectureSnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.MonthlySupplyCapacitySnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.ProductCategorySnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.ProductExpirationTypeSnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.ProductStorySnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.StorageTypeSnapshot;
import com.hanrolink.product.policy.MonthlySupplyCapacityPolicy;
import com.hanrolink.product.repository.MonthlySupplyCapacityRepository;
import com.hanrolink.product.repository.ProductRepository;
import com.hanrolink.product.repository.ProductStoryRepository;
import com.hanrolink.product.repository.projection.ProductSnapshotProjection;

@Service
public class BuyerSentNegotiationRequestService {

  private final ProductNegotiationRequestRepository productNegotiationRequestRepository;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  private final ProductRepository productRepository;

  private final MonthlySupplyCapacityRepository monthlySupplyCapacityRepository;

  private final ProductStoryRepository productStoryRepository;

  public BuyerSentNegotiationRequestService(
    ProductNegotiationRequestRepository productNegotiationRequestRepository,
    BusinessUserAccountRepository businessUserAccountRepository,
    ProductRepository productRepository,
    MonthlySupplyCapacityRepository monthlySupplyCapacityRepository,
    ProductStoryRepository productStoryRepository
  ) {
    this.productNegotiationRequestRepository = productNegotiationRequestRepository;
    this.businessUserAccountRepository = businessUserAccountRepository;
    this.productRepository = productRepository;
    this.monthlySupplyCapacityRepository = monthlySupplyCapacityRepository;
    this.productStoryRepository = productStoryRepository;
  }

  /**
   * 商品に対する商談希望を新規作成する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param productPublicId 商談希望の対象となる商品の公開識別子
   */
  @Transactional
  public void create(
    String identityProviderSubject,
    UUID productPublicId
  ) {
    Long buyerAccountId = businessUserAccountRepository
      .findIdByIdentityProviderSubject(identityProviderSubject)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    Instant currentTime = Instant.now();

    // 有効な商談希望の上限件数確認
    long activeNegotiationRequestCount = productNegotiationRequestRepository
      .countByBuyerAccountIdAndExpiresAtAfter(
        buyerAccountId,
        currentTime
      );
    if (activeNegotiationRequestCount >= ProductNegotiationRequestPolicy.MAX_ACTIVE_REQUEST_COUNT) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "商談希望の送信件数が上限に達しているため、新しく送信できません"
      );
    }

    // 商品の公開状態などの申請可否確認
    boolean isProductAvailable = productRepository
      .existsByPublicIdAndHiddenAtIsNull(productPublicId);
    if (!isProductAvailable) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // 同じ商品に対する有効な商談希望の重複確認
    boolean hasActiveNegotiationRequestForProduct =
      productNegotiationRequestRepository
        .existsActiveByProductPublicIdAndBuyerAccountId(
          productPublicId,
          buyerAccountId,
          currentTime
        );
    if (hasActiveNegotiationRequestForProduct) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "この商品にはすでに有効な商談希望が存在します"
      );
    }

    // 申請時点の商品スナップショットを構成する情報の取得
    ProductSnapshotProjection product = productRepository
      .findSnapshotByPublicId(productPublicId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    List<MonthlySupplyCapacitySnapshot> monthlySupplyCapacities =
      monthlySupplyCapacityRepository.findLatestListByProductId(
        product.id(),
        Pageable.ofSize(MonthlySupplyCapacityPolicy.TARGET_MONTH_COUNT)
      )
      .stream()
      .sorted(
        Comparator.comparing(
          monthlySupplyCapacity -> monthlySupplyCapacity.targetMonth()
        )
      )
      .map(monthlySupplyCapacity ->
        new MonthlySupplyCapacitySnapshot(
          YearMonth.from(
            monthlySupplyCapacity.targetMonth()
          ),
          monthlySupplyCapacity.availableQuantity()
        )
      )
      .toList();
    List<ProductStorySnapshot> productStories = productStoryRepository
      .findSnapshotByProductId(product.id())
      .stream()
      .map(productStory ->
        new ProductStorySnapshot(
          productStory.productStorySectionTemplateId(),
          productStory.sectionTitle(),
          productStory.body()
        )
      )
      .toList();

    // 申請時点の商品スナップショットを保持する商談希望の生成と保存
    ProductNegotiationRequest productNegotiationRequest = new ProductNegotiationRequest(
      buyerAccountId,
      product.id(),
      new ProductSnapshot(
        product.updatedAt(),
        new ProductCategorySnapshot(
          product.productCategoryId(),
          product.productCategoryName()
        ),
        new MainIngredientOriginPrefectureSnapshot(
          product.mainIngredientOriginPrefectureId(),
          product.mainIngredientOriginPrefectureName()
        ),
        product.name(),
        product.contentQuantity(),
        new ProductExpirationTypeSnapshot(
          product.expirationType(),
          product.expirationType().getDisplayName()
        ),
        product.shelfLifeDays(),
        new StorageTypeSnapshot(
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
        monthlySupplyCapacities,
        productStories
      )
    );
    productNegotiationRequestRepository.save(productNegotiationRequest);
  }

  /**
   * バイヤー自身が送信した有効な商品商談希望の一覧を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @return 有効な商品商談希望の一覧
   */
  @Transactional(readOnly = true)
  public List<BuyerSentNegotiationRequestListResponse> list(
    String identityProviderSubject
  ) {
    return productNegotiationRequestRepository
      .findActiveSentListByIdentityProviderSubject(
        identityProviderSubject,
        Instant.now()
      )
      .stream()
      .map(sentNegotiationRequest ->
        new BuyerSentNegotiationRequestListResponse(
          sentNegotiationRequest.publicId(),
          new NegotiationRequestProductResponse(
            sentNegotiationRequest.productPublicId(),
            sentNegotiationRequest.productName()
          ),
          sentNegotiationRequest.expiresAt()
        )
      )
      .toList();
  }
}
