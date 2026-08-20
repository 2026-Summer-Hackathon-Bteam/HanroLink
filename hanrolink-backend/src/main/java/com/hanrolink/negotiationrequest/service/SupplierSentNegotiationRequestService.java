package com.hanrolink.negotiationrequest.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.negotiationrequest.repository.ProcurementNegotiationRequestRepository;
import com.hanrolink.negotiationrequest.response.SupplierSentNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProcurementRequestResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProductResponse;

@Service
public class SupplierSentNegotiationRequestService {

  private final ProcurementNegotiationRequestRepository procurementNegotiationRequestRepository;

  public SupplierSentNegotiationRequestService(
    ProcurementNegotiationRequestRepository procurementNegotiationRequestRepository
  ) {
    this.procurementNegotiationRequestRepository = procurementNegotiationRequestRepository;
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
