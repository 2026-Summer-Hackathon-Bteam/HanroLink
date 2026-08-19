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
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.negotiationrequest.api.SupplierNegotiationRequestApi;
import com.hanrolink.negotiationrequest.response.NegotiationRequestAcceptResponse;
import com.hanrolink.negotiationrequest.response.SupplierReceivedNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.service.SupplierReceivedNegotiationRequestService;
import com.hanrolink.security.authorization.policy.RequiresApprovedSupplier;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class SupplierReceivedNegotiationRequestController {

  private final SupplierReceivedNegotiationRequestService supplierReceivedNegotiationRequestService;

  public SupplierReceivedNegotiationRequestController(
    SupplierReceivedNegotiationRequestService supplierReceivedNegotiationRequestService
  ) {
    this.supplierReceivedNegotiationRequestService = supplierReceivedNegotiationRequestService;
  }

  /**
   * 自社の商品に届いた有効な商談希望一覧を返す
   * @param jwt 認証済みユーザーのJWT
   * @return 自社の商品に届いた有効な商談希望一覧
   */
  @RequiresApprovedSupplier
  @GetMapping(SupplierNegotiationRequestApi.V1.MINE_PRODUCT_NEGOTIATION_REQUESTS)
  public ResponseEntity<List<SupplierReceivedNegotiationRequestListResponse>> list(
    @AuthenticationPrincipal Jwt jwt
  ) {
    return ResponseEntity.ok(
      supplierReceivedNegotiationRequestService.list(jwt.getSubject())
    );
  }

  // 商談希望の宛先となるサプライヤーのみ利用可能
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(SupplierNegotiationRequestApi.V1.ACCEPT)
  public ResponseEntity<NegotiationRequestAcceptResponse> accept(
    @PathVariable UUID productNegotiationRequestId
  ) {

    // TODO: 自社商品に届いた商談希望を承諾し、チャンネルを作成して、responseと201 Createdで返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
