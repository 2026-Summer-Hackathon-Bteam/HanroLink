package com.hanrolink.negotiationrequest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.negotiationrequest.api.BuyerNegotiationRequestApi;
import com.hanrolink.negotiationrequest.response.BuyerProductNegotiationRequestListResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class BuyerProductNegotiationRequestController {

  // バイヤーのみ利用可能
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(BuyerNegotiationRequestApi.V1.CREATE)
  public ResponseEntity<Void> create(
    @PathVariable Long productId
  ) {

    // TODO: バイヤーが商品に対して商談希望を登録して、201 Createdで返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  // 登録元のバイヤーのみ利用可能
  @GetMapping(BuyerNegotiationRequestApi.V1.MINE_PRODUCT_NEGOTIATION_REQUESTS)
  public ResponseEntity<List<BuyerProductNegotiationRequestListResponse>> list() {

    // TODO: バイヤー自身が送った未承諾の商談希望一覧を取得して、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
