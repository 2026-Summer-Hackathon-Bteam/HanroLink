package com.hanrolink.negotiationrequest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.negotiationrequest.api.BuyerNegotiationRequestApi;
import com.hanrolink.negotiationrequest.response.NegotiationRequestAcceptResponse;
import com.hanrolink.negotiationrequest.response.BuyerReceivedNegotiationRequestListResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class BuyerReceivedNegotiationRequestController {

  // 登録元のバイヤーのみ利用可能
  @GetMapping(BuyerNegotiationRequestApi.V1.MINE_PROCUREMENT_NEGOTIATION_REQUESTS)
  public ResponseEntity<List<BuyerReceivedNegotiationRequestListResponse>> list() {

    // TODO: 自社募集に届いた未承諾の商談希望一覧を取得して、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  // 商談希望の宛先となるバイヤーのみ利用可能
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(BuyerNegotiationRequestApi.V1.ACCEPT)
  public ResponseEntity<NegotiationRequestAcceptResponse> accept(
    @PathVariable UUID procurementNegotiationRequestId
  ) {

    // TODO: 自社募集に届いた商談希望を承諾し、チャンネルを作成して、responseと201 Createdで返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
