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
import com.hanrolink.negotiationrequest.entity.ProcurementNegotiationRequest;
import com.hanrolink.negotiationrequest.policy.SupplierNegotiationRequestPolicy;
import com.hanrolink.negotiationrequest.repository.ProcurementNegotiationRequestRepository;
import com.hanrolink.negotiationrequest.request.SupplierSentNegotiationRequestCreateRequest;
import com.hanrolink.negotiationrequest.response.SupplierSentNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProcurementRequestResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProductResponse;
import com.hanrolink.negotiationrequest.snapshot.ProcurementRequestSnapshot;
import com.hanrolink.negotiationrequest.snapshot.ProductSnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.MainIngredientOriginPrefectureSnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.MonthlyProcurementQuantitySnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.MonthlySupplyCapacitySnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.ProductCategorySnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.ProductExpirationTypeSnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.ProductStorySnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.StorageTypeSnapshot;
import com.hanrolink.procurementrequest.policy.MonthlyProcurementQuantityPolicy;
import com.hanrolink.procurementrequest.repository.MonthlyProcurementQuantityRepository;
import com.hanrolink.procurementrequest.repository.ProcurementRequestRepository;
import com.hanrolink.procurementrequest.repository.ProcurementRequestStorageTypeRepository;
import com.hanrolink.procurementrequest.repository.projection.ProcurementRequestSnapshotProjection;
import com.hanrolink.product.policy.MonthlySupplyCapacityPolicy;
import com.hanrolink.product.repository.MonthlySupplyCapacityRepository;
import com.hanrolink.product.repository.ProductRepository;
import com.hanrolink.product.repository.ProductStoryRepository;
import com.hanrolink.product.repository.projection.ProductSnapshotProjection;

@Service
public class SupplierSentNegotiationRequestService {

  private final ProcurementNegotiationRequestRepository procurementNegotiationRequestRepository;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  private final ProcurementRequestRepository procurementRequestRepository;

  private final ProcurementRequestStorageTypeRepository procurementRequestStorageTypeRepository;

  private final MonthlyProcurementQuantityRepository monthlyProcurementQuantityRepository;

  private final ProductRepository productRepository;

  private final MonthlySupplyCapacityRepository monthlySupplyCapacityRepository;

  private final ProductStoryRepository productStoryRepository;

  public SupplierSentNegotiationRequestService(
    ProcurementNegotiationRequestRepository procurementNegotiationRequestRepository,
    BusinessUserAccountRepository businessUserAccountRepository,
    ProcurementRequestRepository procurementRequestRepository,
    ProcurementRequestStorageTypeRepository procurementRequestStorageTypeRepository,
    MonthlyProcurementQuantityRepository monthlyProcurementQuantityRepository,
    ProductRepository productRepository,
    MonthlySupplyCapacityRepository monthlySupplyCapacityRepository,
    ProductStoryRepository productStoryRepository
  ) {
    this.procurementNegotiationRequestRepository = procurementNegotiationRequestRepository;
    this.businessUserAccountRepository = businessUserAccountRepository;
    this.procurementRequestRepository = procurementRequestRepository;
    this.procurementRequestStorageTypeRepository = procurementRequestStorageTypeRepository;
    this.monthlyProcurementQuantityRepository = monthlyProcurementQuantityRepository;
    this.productRepository = productRepository;
    this.monthlySupplyCapacityRepository = monthlySupplyCapacityRepository;
    this.productStoryRepository = productStoryRepository;
  }

  /**
   * 募集に対する商談希望を新規作成する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param procurementRequestPublicId 商談希望の対象となる募集の公開識別子
   * @param request 商談希望で提示する商品の情報
   */
  @Transactional
  public void create(
    String identityProviderSubject,
    UUID procurementRequestPublicId,
    SupplierSentNegotiationRequestCreateRequest request
  ) {
    Long supplierAccountId = businessUserAccountRepository
      .findIdByIdentityProviderSubject(identityProviderSubject)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    Instant currentTime = Instant.now();

    // 有効な商談希望の上限件数確認
    long activeNegotiationRequestCount = procurementNegotiationRequestRepository
      .countBySupplierAccountIdAndExpiresAtAfter(
        supplierAccountId,
        currentTime
      );
    if (activeNegotiationRequestCount >= SupplierNegotiationRequestPolicy.MAX_ACTIVE_REQUEST_COUNT) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "商談希望の送信件数が上限に達しているため、新しく送信できません"
      );
    }

    // 申請対象となる募集の存在確認
    boolean isAvailableProcurementRequest = procurementRequestRepository
      .existsByPublicId(procurementRequestPublicId);
    if (!isAvailableProcurementRequest) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // 提示商品の所有関係と公開状態の確認
    boolean isProductAvailable = productRepository
      .existsVisibleByPublicIdAndSupplierAccountId(
        request.productId(),
        supplierAccountId
      );
    if (!isProductAvailable) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // 同じ募集に対する有効な商談希望の重複確認
    boolean hasActiveNegotiationRequestForProcurementRequest =
      procurementNegotiationRequestRepository
        .existsActiveByProcurementRequestPublicIdAndSupplierAccountId(
          procurementRequestPublicId,
          supplierAccountId,
          currentTime
        );
    if (hasActiveNegotiationRequestForProcurementRequest) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "この募集にはすでに有効な商談希望が存在します"
      );
    }

    // 申請時点の募集スナップショットを構成する情報の取得
    ProcurementRequestSnapshotProjection procurementRequest =
      procurementRequestRepository
        .findSnapshotByPublicId(procurementRequestPublicId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    List<StorageTypeSnapshot> storageTypes =
      procurementRequestStorageTypeRepository
        .findStorageTypesByProcurementRequestId(procurementRequest.id())
        .stream()
        .map(storageType ->
          new StorageTypeSnapshot(
            storageType,
            storageType.getDisplayName()
          )
        )
        .toList();
    List<MonthlyProcurementQuantitySnapshot> monthlyProcurementQuantities =
      monthlyProcurementQuantityRepository.findLatestListByProcurementRequestId(
        procurementRequest.id(),
        Pageable.ofSize(MonthlyProcurementQuantityPolicy.TARGET_MONTH_COUNT)
      )
      .stream()
      .sorted(
        Comparator.comparing(
          monthlyProcurementQuantity -> monthlyProcurementQuantity.targetMonth()
        )
      )
      .map(monthlyProcurementQuantity ->
        new MonthlyProcurementQuantitySnapshot(
          YearMonth.from(
            monthlyProcurementQuantity.targetMonth()
          ),
          monthlyProcurementQuantity.desiredQuantity()
        )
      )
      .toList();

    // 申請時点の商品スナップショットを構成する情報の取得
    ProductSnapshotProjection product = productRepository
      .findSnapshotByPublicId(request.productId())
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

    // 申請時点の募集・商品スナップショットを保持する商談希望の生成と保存
    ProcurementNegotiationRequest procurementNegotiationRequest =
      new ProcurementNegotiationRequest(
        supplierAccountId,
        procurementRequest.id(),
        product.id(),
        new ProcurementRequestSnapshot(
          procurementRequest.updatedAt(),
          new ProductCategorySnapshot(
            procurementRequest.productCategoryId(),
            procurementRequest.productCategoryName()
          ),
          procurementRequest.title(),
          procurementRequest.description(),
          procurementRequest.requiredTradeTerms(),
          procurementRequest.desiredUnitPrice(),
          procurementRequest.deliveryShelfLifeDays(),
          storageTypes,
          monthlyProcurementQuantities
        ),
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
    procurementNegotiationRequestRepository.save(procurementNegotiationRequest);
  }

  /**
   * サプライヤー自身が送信した有効な募集商談希望の一覧を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @return 有効な募集商談希望の一覧
   */
  @Transactional(readOnly = true)
  public List<SupplierSentNegotiationRequestListResponse> list(
    String identityProviderSubject
  ) {
    return procurementNegotiationRequestRepository
      .findActiveSentListByIdentityProviderSubject(
        identityProviderSubject,
        Instant.now()
      )
      .stream()
      .map(sentNegotiationRequest ->
        new SupplierSentNegotiationRequestListResponse(
          sentNegotiationRequest.publicId(),
          new NegotiationRequestProcurementRequestResponse(
            sentNegotiationRequest.procurementRequestPublicId(),
            sentNegotiationRequest.procurementRequestTitle()
          ),
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
