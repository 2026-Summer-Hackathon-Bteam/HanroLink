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
import com.hanrolink.negotiationrequest.request.SupplierSentNegotiationRequestCreateRequest;
import com.hanrolink.negotiationrequest.response.SupplierSentNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.service.SupplierSentNegotiationRequestService;
import com.hanrolink.security.authorization.policy.RequiresApprovedSupplier;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class SupplierSentNegotiationRequestController {

  private final SupplierSentNegotiationRequestService supplierSentNegotiationRequestService;

  public SupplierSentNegotiationRequestController(
    SupplierSentNegotiationRequestService supplierSentNegotiationRequestService
  ) {
    this.supplierSentNegotiationRequestService = supplierSentNegotiationRequestService;
  }

  /**
   * 募集に対する商談希望の新規作成を受け付ける
   * @param jwt 認証済みユーザーのJWT
   * @param procurementRequestId 商談希望の対象となる募集の公開識別子
   * @param request 商談希望で提示する商品の情報
   * @return 作成結果
   */
  @RequiresApprovedSupplier
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(SupplierNegotiationRequestApi.V1.CREATE)
  public ResponseEntity<Void> create(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable UUID procurementRequestId,
    @Valid @RequestBody SupplierSentNegotiationRequestCreateRequest request
  ) {
    supplierSentNegotiationRequestService.create(
      jwt.getSubject(),
      procurementRequestId,
      request
    );

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * サプライヤー自身が送信した有効な募集商談希望一覧を返す
   * @param jwt 認証済みユーザーのJWT
   * @return 有効な募集商談希望の一覧
   */
  @RequiresApprovedSupplier
  @GetMapping(SupplierNegotiationRequestApi.V1.MINE_PROCUREMENT_NEGOTIATION_REQUESTS)
  public ResponseEntity<List<SupplierSentNegotiationRequestListResponse>> list(
    @AuthenticationPrincipal Jwt jwt
  ) {
    return ResponseEntity.ok(
      supplierSentNegotiationRequestService.list(jwt.getSubject())
    );
  }
}
