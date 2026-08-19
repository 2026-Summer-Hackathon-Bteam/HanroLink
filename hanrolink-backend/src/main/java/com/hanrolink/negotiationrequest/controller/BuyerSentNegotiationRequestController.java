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

import com.hanrolink.negotiationrequest.api.BuyerNegotiationRequestApi;
import com.hanrolink.negotiationrequest.response.BuyerSentNegotiationRequestListResponse;
import com.hanrolink.negotiationrequest.service.BuyerSentNegotiationRequestService;
import com.hanrolink.security.authorization.policy.RequiresApprovedBuyer;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class BuyerSentNegotiationRequestController {

  private final BuyerSentNegotiationRequestService buyerSentNegotiationRequestService;

  public BuyerSentNegotiationRequestController(
    BuyerSentNegotiationRequestService buyerSentNegotiationRequestService
  ) {
    this.buyerSentNegotiationRequestService = buyerSentNegotiationRequestService;
  }

  // バイヤーのみ利用可能
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(BuyerNegotiationRequestApi.V1.CREATE)
  public ResponseEntity<Void> create(
    @PathVariable UUID productId
  ) {

    // TODO: バイヤーが商品に対して商談希望を登録して、201 Createdで返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  /**
   * バイヤー自身が送信した有効な募集商談希望一覧の取得を受け付ける
   * @param jwt 認証済みユーザーのJWT
   * @return 有効な商品商談希望の一覧
   */
  @RequiresApprovedBuyer
  @GetMapping(BuyerNegotiationRequestApi.V1.MINE_PRODUCT_NEGOTIATION_REQUESTS)
  public ResponseEntity<List<BuyerSentNegotiationRequestListResponse>> list(
    @AuthenticationPrincipal Jwt jwt
  ) {
    return ResponseEntity.ok(
      buyerSentNegotiationRequestService.list(jwt.getSubject())
    );
  }
}
