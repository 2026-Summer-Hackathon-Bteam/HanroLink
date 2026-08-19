package com.hanrolink.negotiationrequest.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.negotiationrequest.policy.NegotiationRequestPolicy;
import com.hanrolink.negotiationrequest.repository.ProcurementNegotiationRequestRepository;
import com.hanrolink.negotiationrequest.response.BuyerReceivedNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.response.component.BuyerReceivedNegotiationRequestProductResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProcurementRequestResponse;

@Service
public class BuyerReceivedNegotiationRequestService {

  private final ProcurementNegotiationRequestRepository procurementNegotiationRequestRepository;

  public BuyerReceivedNegotiationRequestService(
    ProcurementNegotiationRequestRepository procurementNegotiationRequestRepository
  ) {
    this.procurementNegotiationRequestRepository = procurementNegotiationRequestRepository;
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
    Instant activeSince = Instant.now().minus(
      NegotiationRequestPolicy.ACTIVE_PERIOD_DAYS,
      ChronoUnit.DAYS
    );

    return procurementNegotiationRequestRepository
      .findActiveReceivedListByIdentityProviderSubject(
        identityProviderSubject,
        activeSince
      )
      .stream()
      .map(receivedNegotiationRequest ->
        new BuyerReceivedNegotiationRequestListResponse(
          receivedNegotiationRequest.publicId(),
          new NegotiationRequestProcurementRequestResponse(
            receivedNegotiationRequest.procurementRequestPublicId(),
            receivedNegotiationRequest.procurementRequestTitle()
          ),
          new BuyerReceivedNegotiationRequestProductResponse(
            receivedNegotiationRequest.productPublicId(),
            receivedNegotiationRequest.productName(),
            receivedNegotiationRequest.productOwnerBusinessName()
          ),
          receivedNegotiationRequest.createdAt().plus(
            NegotiationRequestPolicy.ACTIVE_PERIOD_DAYS,
            ChronoUnit.DAYS
          )
        )
      )
      .toList();
  }
}
