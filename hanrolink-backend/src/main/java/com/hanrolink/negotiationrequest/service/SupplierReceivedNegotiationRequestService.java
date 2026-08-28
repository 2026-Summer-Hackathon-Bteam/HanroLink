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
import com.hanrolink.negotiationrequest.entity.ProductNegotiationRequest;
import com.hanrolink.negotiationrequest.repository.ProductNegotiationRequestRepository;
import com.hanrolink.negotiationrequest.response.NegotiationRequestAcceptResponse;
import com.hanrolink.negotiationrequest.response.SupplierReceivedNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestChannelResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProductResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestSenderBusinessResponse;
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
public class SupplierReceivedNegotiationRequestService {

  private final ProductNegotiationRequestRepository productNegotiationRequestRepository;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  private final ProductRepository productRepository;

  private final MonthlySupplyCapacityRepository monthlySupplyCapacityRepository;

  private final ProductStoryRepository productStoryRepository;

  private final ChannelRepository channelRepository;

  public SupplierReceivedNegotiationRequestService(
    ProductNegotiationRequestRepository productNegotiationRequestRepository,
    BusinessUserAccountRepository businessUserAccountRepository,
    ProductRepository productRepository,
    MonthlySupplyCapacityRepository monthlySupplyCapacityRepository,
    ProductStoryRepository productStoryRepository,
    ChannelRepository channelRepository
  ) {
    this.productNegotiationRequestRepository = productNegotiationRequestRepository;
    this.businessUserAccountRepository = businessUserAccountRepository;
    this.productRepository = productRepository;
    this.monthlySupplyCapacityRepository = monthlySupplyCapacityRepository;
    this.productStoryRepository = productStoryRepository;
    this.channelRepository = channelRepository;
  }

  /**
   * 自社の商品に届いた有効な商談希望一覧を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @return 自社の商品に届いた有効な商談希望一覧
   */
  @Transactional(readOnly = true)
  public List<SupplierReceivedNegotiationRequestListResponse> list(
    String identityProviderSubject
  ) {
    return productNegotiationRequestRepository
      .findActiveReceivedListByIdentityProviderSubject(
        identityProviderSubject,
        Instant.now()
      )
      .stream()
      .map(receivedNegotiationRequest ->
        new SupplierReceivedNegotiationRequestListResponse(
          receivedNegotiationRequest.publicId(),
          new NegotiationRequestProductResponse(
            receivedNegotiationRequest.productPublicId(),
            receivedNegotiationRequest.productName()
          ),
          new NegotiationRequestSenderBusinessResponse(
            receivedNegotiationRequest.senderBusinessPublicId(),
            receivedNegotiationRequest.senderBusinessName()
          ),
          receivedNegotiationRequest.expiresAt()
        )
      )
      .toList();
  }

  /**
   * 自社の商品に届いた商談希望を承諾してチャンネルを作成する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param productNegotiationRequestPublicId 承諾対象となる商談希望の公開識別子
   * @return 作成したチャンネル情報を含む承諾結果
   */
  @Transactional
  public NegotiationRequestAcceptResponse accept(
    String identityProviderSubject,
    UUID productNegotiationRequestPublicId
  ) {
    // 承諾対象となる有効な商談希望の取得
    ProductNegotiationRequest productNegotiationRequest =
      productNegotiationRequestRepository
        .findActiveReceivedByPublicIdAndIdentityProviderSubject(
          productNegotiationRequestPublicId,
          identityProviderSubject,
          Instant.now()
        )
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 承諾時点の商品スナップショットを構成する情報の取得
    ProductSnapshotProjection acceptedProduct = productRepository
      .findSnapshotById(productNegotiationRequest.getProductId())
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

    // 希望時点・承諾時点の商品スナップショットを保持するチャンネルの生成と保存
    Long supplierAccountId = businessUserAccountRepository
      .findIdByIdentityProviderSubject(identityProviderSubject)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    Channel channel = new Channel(
      supplierAccountId,
      productNegotiationRequest.getBuyerAccountId(),
      acceptedProduct.name(),
      NegotiationTargetType.PRODUCT,
      productNegotiationRequest.getProductSnapshot(),
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
      null,
      null
    );
    Channel savedChannel = channelRepository.save(channel);

    // チャンネル作成済みの商談希望の削除
    productNegotiationRequestRepository.delete(productNegotiationRequest);

    // 作成したチャンネル情報を含む承諾結果の生成
    String counterpartyBusinessName = businessUserAccountRepository
      .findBusinessNameById(savedChannel.getBuyerAccountId())
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
