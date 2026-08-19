package com.hanrolink.negotiationrequest.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.negotiationrequest.policy.NegotiationRequestPolicy;
import com.hanrolink.negotiationrequest.repository.ProductNegotiationRequestRepository;
import com.hanrolink.negotiationrequest.response.SupplierReceivedNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProductResponse;
import com.hanrolink.negotiationrequest.response.component.SupplierReceivedNegotiationRequestBuyerResponse;

@Service
public class SupplierReceivedNegotiationRequestService {

  private final ProductNegotiationRequestRepository productNegotiationRequestRepository;

  public SupplierReceivedNegotiationRequestService(
    ProductNegotiationRequestRepository productNegotiationRequestRepository
  ) {
    this.productNegotiationRequestRepository = productNegotiationRequestRepository;
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
    Instant activeSince = Instant.now().minus(
      NegotiationRequestPolicy.ACTIVE_PERIOD_DAYS,
      ChronoUnit.DAYS
    );

    return productNegotiationRequestRepository
      .findActiveReceivedListByIdentityProviderSubject(
        identityProviderSubject,
        activeSince
      )
      .stream()
      .map(receivedNegotiationRequest ->
        new SupplierReceivedNegotiationRequestListResponse(
          receivedNegotiationRequest.publicId(),
          new NegotiationRequestProductResponse(
            receivedNegotiationRequest.productPublicId(),
            receivedNegotiationRequest.productName()
          ),
          new SupplierReceivedNegotiationRequestBuyerResponse(
            receivedNegotiationRequest.senderBusinessPublicId(),
            receivedNegotiationRequest.senderBusinessName()
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
