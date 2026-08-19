package com.hanrolink.negotiationrequest.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.negotiationrequest.policy.NegotiationRequestPolicy;
import com.hanrolink.negotiationrequest.repository.ProductNegotiationRequestRepository;
import com.hanrolink.negotiationrequest.response.BuyerSentNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProductResponse;

@Service
public class BuyerSentNegotiationRequestService {

  private final ProductNegotiationRequestRepository productNegotiationRequestRepository;

  public BuyerSentNegotiationRequestService (
    ProductNegotiationRequestRepository productNegotiationRequestRepository
  ) {
    this.productNegotiationRequestRepository = productNegotiationRequestRepository;
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
    Instant activeSince = Instant.now().minus(
      NegotiationRequestPolicy.ACTIVE_PERIOD_DAYS,
      ChronoUnit.DAYS
    );

    return productNegotiationRequestRepository
      .findActiveSentListByIdentityProviderSubject(
        identityProviderSubject,
        activeSince
      )
      .stream()
      .map(sentNegotiationRequest ->
        new BuyerSentNegotiationRequestListResponse(
          sentNegotiationRequest.publicId(),
          new NegotiationRequestProductResponse(
            sentNegotiationRequest.productPublicId(),
            sentNegotiationRequest.productName()
          ),
          sentNegotiationRequest.createdAt()
            .plus(
              NegotiationRequestPolicy.ACTIVE_PERIOD_DAYS,
              ChronoUnit.DAYS
            )
        )
      )
      .toList();
  }
}
