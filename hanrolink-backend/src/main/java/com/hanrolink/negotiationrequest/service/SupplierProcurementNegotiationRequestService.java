package com.hanrolink.negotiationrequest.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.negotiationrequest.policy.NegotiationRequestPolicy;
import com.hanrolink.negotiationrequest.repository.ProcurementNegotiationRequestRepository;
import com.hanrolink.negotiationrequest.repository.projection.SupplierProcurementNegotiationRequestListProjection;
import com.hanrolink.negotiationrequest.response.SupplierProcurementNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProcurementRequestResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProductResponse;

@Service
public class SupplierProcurementNegotiationRequestService {

  private final ProcurementNegotiationRequestRepository procurementNegotiationRequestRepository;

  public SupplierProcurementNegotiationRequestService(
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
  public List<SupplierProcurementNegotiationRequestListResponse> list(
    String identityProviderSubject
  ) {
    Instant activeSince = Instant.now().minus(
      NegotiationRequestPolicy.ACTIVE_PERIOD_DAYS,
      ChronoUnit.DAYS
    );
    List<SupplierProcurementNegotiationRequestListProjection> activeProcurementNegotiationRequests =
      procurementNegotiationRequestRepository
        .findActiveListByIdentityProviderSubject(
          identityProviderSubject,
          activeSince
        );

    return activeProcurementNegotiationRequests
      .stream()
      .map(activeProcurementNegotiationRequest ->
        new SupplierProcurementNegotiationRequestListResponse(
          activeProcurementNegotiationRequest.publicId(),
          new NegotiationRequestProcurementRequestResponse(
            activeProcurementNegotiationRequest.procurementRequestPublicId(),
            activeProcurementNegotiationRequest.procurementRequestTitle()
          ),
          new NegotiationRequestProductResponse(
            activeProcurementNegotiationRequest.productPublicId(),
            activeProcurementNegotiationRequest.productName()
          ),
          activeProcurementNegotiationRequest.createdAt().plus(
            NegotiationRequestPolicy.ACTIVE_PERIOD_DAYS,
            ChronoUnit.DAYS
          )
        )
      )
      .toList();
  }
}
