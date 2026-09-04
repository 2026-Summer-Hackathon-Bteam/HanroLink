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
import com.hanrolink.chat.entity.Channel;
import com.hanrolink.chat.enums.NegotiationTargetType;
import com.hanrolink.chat.repository.ChannelRepository;
import com.hanrolink.negotiationrequest.entity.ProcurementNegotiationRequest;
import com.hanrolink.negotiationrequest.repository.ProcurementNegotiationRequestRepository;
import com.hanrolink.negotiationrequest.response.BuyerReceivedNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.response.NegotiationRequestAcceptResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestChannelResponse;
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
public class BuyerReceivedNegotiationRequestService {

  private final ProcurementNegotiationRequestRepository procurementNegotiationRequestRepository;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  private final ProcurementRequestRepository procurementRequestRepository;

  private final ProcurementRequestStorageTypeRepository procurementRequestStorageTypeRepository;

  private final MonthlyProcurementQuantityRepository monthlyProcurementQuantityRepository;

  private final ProductRepository productRepository;

  private final MonthlySupplyCapacityRepository monthlySupplyCapacityRepository;

  private final ProductStoryRepository productStoryRepository;

  private final ChannelRepository channelRepository;

  public BuyerReceivedNegotiationRequestService(
    ProcurementNegotiationRequestRepository procurementNegotiationRequestRepository,
    BusinessUserAccountRepository businessUserAccountRepository,
    ProcurementRequestRepository procurementRequestRepository,
    ProcurementRequestStorageTypeRepository procurementRequestStorageTypeRepository,
    MonthlyProcurementQuantityRepository monthlyProcurementQuantityRepository,
    ProductRepository productRepository,
    MonthlySupplyCapacityRepository monthlySupplyCapacityRepository,
    ProductStoryRepository productStoryRepository,
    ChannelRepository channelRepository
  ) {
    this.procurementNegotiationRequestRepository = procurementNegotiationRequestRepository;
    this.businessUserAccountRepository = businessUserAccountRepository;
    this.procurementRequestRepository = procurementRequestRepository;
    this.procurementRequestStorageTypeRepository = procurementRequestStorageTypeRepository;
    this.monthlyProcurementQuantityRepository = monthlyProcurementQuantityRepository;
    this.productRepository = productRepository;
    this.monthlySupplyCapacityRepository = monthlySupplyCapacityRepository;
    this.productStoryRepository = productStoryRepository;
    this.channelRepository = channelRepository;
  }

  /**
   * 自社の募集に届いた有効な商談希望一覧を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @return 自社の募集に届いた有効な商談希望一覧
   */
  @Transactional(readOnly = true)
  public List<BuyerReceivedNegotiationRequestListResponse> list(
    String identityProviderSubject
  ) {
    return procurementNegotiationRequestRepository
      .findActiveReceivedListByIdentityProviderSubject(
        identityProviderSubject,
        Instant.now()
      )
      .stream()
      .map(receivedNegotiationRequest ->
        new BuyerReceivedNegotiationRequestListResponse(
          receivedNegotiationRequest.publicId(),
          new NegotiationRequestProcurementRequestResponse(
            receivedNegotiationRequest.procurementRequestPublicId(),
            receivedNegotiationRequest.procurementRequestTitle()
          ),
          new NegotiationRequestProductResponse(
            receivedNegotiationRequest.productPublicId(),
            receivedNegotiationRequest.productName()
          ),
          receivedNegotiationRequest.senderBusinessName(),
          receivedNegotiationRequest.expiresAt()
        )
      )
      .toList();
  }

  /**
   * 自社の募集に届いた商談希望を承諾してチャンネルを作成する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param procurementNegotiationRequestPublicId 承諾対象となる商談希望の公開識別子
   * @return 作成したチャンネル情報を含む承諾結果
   */
  @Transactional
  public NegotiationRequestAcceptResponse accept(
    String identityProviderSubject,
    UUID procurementNegotiationRequestPublicId
  ) {
    // 承諾対象となる有効な商談希望の取得
    ProcurementNegotiationRequest procurementNegotiationRequest =
      procurementNegotiationRequestRepository
        .findActiveReceivedByPublicIdAndIdentityProviderSubject(
          procurementNegotiationRequestPublicId,
          identityProviderSubject,
          Instant.now()
        )
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 承諾時点の募集スナップショットを構成する情報の取得
    ProcurementRequestSnapshotProjection acceptedProcurementRequest =
      procurementRequestRepository
        .findSnapshotById(procurementNegotiationRequest.getProcurementRequestId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    List<StorageTypeSnapshot> acceptedStorageTypes =
      procurementRequestStorageTypeRepository
        .findStorageTypesByProcurementRequestId(acceptedProcurementRequest.id())
        .stream()
        .map(storageType ->
          new StorageTypeSnapshot(
            storageType,
            storageType.getDisplayName()
          )
        )
        .toList();
    List<MonthlyProcurementQuantitySnapshot> acceptedMonthlyProcurementQuantities =
      monthlyProcurementQuantityRepository.findLatestListByProcurementRequestId(
        acceptedProcurementRequest.id(),
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

    // 承諾時点の商品スナップショットを構成する情報の取得
    ProductSnapshotProjection acceptedProduct = productRepository
      .findSnapshotById(procurementNegotiationRequest.getProductId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    List<MonthlySupplyCapacitySnapshot> acceptedMonthlySupplyCapacities =
      monthlySupplyCapacityRepository.findLatestListByProductId(
        acceptedProduct.id(),
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
    List<ProductStorySnapshot> acceptedProductStories = productStoryRepository
      .findSnapshotByProductId(acceptedProduct.id())
      .stream()
      .map(productStory ->
        new ProductStorySnapshot(
          productStory.productStorySectionTemplateId(),
          productStory.sectionTitle(),
          productStory.body()
        )
      )
      .toList();

    // 希望時点・承諾時点の募集、商品スナップショットを保持するチャンネルの生成と保存
    Long buyerAccountId = businessUserAccountRepository
      .findIdByIdentityProviderSubject(identityProviderSubject)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    Channel channel = new Channel(
      procurementNegotiationRequest.getSupplierAccountId(),
      buyerAccountId,
      acceptedProcurementRequest.title(),
      NegotiationTargetType.PROCUREMENT_REQUEST,
      procurementNegotiationRequest.getProductSnapshot(),
      new ProductSnapshot(
        acceptedProduct.updatedAt(),
        new ProductCategorySnapshot(
          acceptedProduct.productCategoryId(),
          acceptedProduct.productCategoryName()
        ),
        new MainIngredientOriginPrefectureSnapshot(
          acceptedProduct.mainIngredientOriginPrefectureId(),
          acceptedProduct.mainIngredientOriginPrefectureName()
        ),
        acceptedProduct.name(),
        acceptedProduct.contentQuantity(),
        new ProductExpirationTypeSnapshot(
          acceptedProduct.expirationType(),
          acceptedProduct.expirationType().getDisplayName()
        ),
        acceptedProduct.shelfLifeDays(),
        new StorageTypeSnapshot(
          acceptedProduct.storageType(),
          acceptedProduct.storageType().getDisplayName()
        ),
        acceptedProduct.desiredRetailPrice(),
        acceptedProduct.allergyInformation(),
        acceptedProduct.certificationInformation(),
        acceptedProduct.caseSize(),
        acceptedProduct.unitsPerCase(),
        acceptedProduct.minimumOrderQuantity(),
        acceptedProduct.shippingLeadTimeDays(),
        acceptedProduct.salesAreaRestriction(),
        acceptedMonthlySupplyCapacities,
        acceptedProductStories
      ),
      procurementNegotiationRequest.getProcurementRequestSnapshot(),
      new ProcurementRequestSnapshot(
        acceptedProcurementRequest.updatedAt(),
        new ProductCategorySnapshot(
          acceptedProcurementRequest.productCategoryId(),
          acceptedProcurementRequest.productCategoryName()
        ),
        acceptedProcurementRequest.title(),
        acceptedProcurementRequest.description(),
        acceptedProcurementRequest.requiredTradeTerms(),
        acceptedProcurementRequest.desiredUnitPrice(),
        acceptedProcurementRequest.deliveryShelfLifeDays(),
        acceptedStorageTypes,
        acceptedMonthlyProcurementQuantities
      )
    );
    Channel savedChannel = channelRepository.save(channel);

    // チャンネル作成済みの商談希望の削除
    procurementNegotiationRequestRepository.delete(procurementNegotiationRequest);

    // 作成したチャンネル情報を含む承諾結果の生成
    String counterpartyBusinessName = businessUserAccountRepository
      .findBusinessNameById(savedChannel.getSupplierAccountId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return new NegotiationRequestAcceptResponse(
      new NegotiationRequestChannelResponse(
        savedChannel.getPublicId(),
        savedChannel.getName(),
        counterpartyBusinessName,
        savedChannel.getUpdatedAt()
      )
    );
  }
}
