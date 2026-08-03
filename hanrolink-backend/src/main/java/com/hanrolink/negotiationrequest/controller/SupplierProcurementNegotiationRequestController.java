package com.hanrolink.negotiationrequest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.negotiationrequest.api.SupplierNegotiationRequestApi;
import com.hanrolink.negotiationrequest.request.SupplierProcurementNegotiationRequestCreateRequest;
import com.hanrolink.negotiationrequest.response.SupplierProcurementNegotiationRequestListResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class SupplierProcurementNegotiationRequestController {

  // サプライヤーのみ利用可能
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(SupplierNegotiationRequestApi.V1.CREATE)
  public ResponseEntity<Void> create(
    @PathVariable Long procurementRequestId,
    @Valid @RequestBody SupplierProcurementNegotiationRequestCreateRequest request
  ) {

    // TODO: サプライヤーが募集に対して商談希望を登録して、201 Createdで返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  // 登録元のサプライヤーのみ利用可能
  @GetMapping(SupplierNegotiationRequestApi.V1.MINE_PROCUREMENT_NEGOTIATION_REQUESTS)
  public ResponseEntity<List<SupplierProcurementNegotiationRequestListResponse>> list() {

    // TODO: サプライヤー自身が送った未承諾の商談希望一覧を取得して、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
