package com.hanrolink.negotiationrequest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.negotiationrequest.api.SupplierNegotiationRequestApi;
import com.hanrolink.negotiationrequest.request.SupplierProcurementNegotiationRequestCreateRequest;
import com.hanrolink.negotiationrequest.response.SupplierProcurementNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.service.SupplierProcurementNegotiationRequestService;
import com.hanrolink.security.authorization.policy.RequiresApprovedSupplier;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class SupplierProcurementNegotiationRequestController {

  private final SupplierProcurementNegotiationRequestService supplierProcurementNegotiationRequestService;

  public SupplierProcurementNegotiationRequestController(
    SupplierProcurementNegotiationRequestService supplierProcurementNegotiationRequestService
  ) {
    this.supplierProcurementNegotiationRequestService = supplierProcurementNegotiationRequestService;
  }

  // サプライヤーのみ利用可能
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(SupplierNegotiationRequestApi.V1.CREATE)
  public ResponseEntity<Void> create(
    @PathVariable UUID procurementRequestId,
    @Valid @RequestBody SupplierProcurementNegotiationRequestCreateRequest request
  ) {

    // TODO: サプライヤーが募集に対して商談希望を登録して、201 Createdで返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  /**
   * サプライヤー自身が送信した有効な募集商談希望一覧の取得を受け付ける
   * @param jwt 認証済みユーザーのJWT
   * @return 有効な募集商談希望の一覧
   */
  @RequiresApprovedSupplier
  @GetMapping(SupplierNegotiationRequestApi.V1.MINE_PROCUREMENT_NEGOTIATION_REQUESTS)
  public ResponseEntity<List<SupplierProcurementNegotiationRequestListResponse>> list(
    @AuthenticationPrincipal Jwt jwt
  ) {
    return ResponseEntity.ok(
      supplierProcurementNegotiationRequestService.list(jwt.getSubject())
    );
  }
}
