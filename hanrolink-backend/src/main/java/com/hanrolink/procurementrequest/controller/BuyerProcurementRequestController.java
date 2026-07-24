package com.hanrolink.procurementrequest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.procurementrequest.api.BuyerProcurementRequestApi;
import com.hanrolink.procurementrequest.request.BuyerProcurementRequestCreateRequest;
import com.hanrolink.procurementrequest.request.BuyerProcurementRequestUpdateRequest;
import com.hanrolink.procurementrequest.response.BuyerProcurementRequestCreateResponse;
import com.hanrolink.procurementrequest.response.BuyerProcurementRequestListResponse;
import com.hanrolink.procurementrequest.response.ProcurementRequestDetailResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class BuyerProcurementRequestController {

  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(BuyerProcurementRequestApi.V1.BASE)
  public ResponseEntity<BuyerProcurementRequestCreateResponse> create(
    @Valid @RequestBody BuyerProcurementRequestCreateRequest request
  ) {

    // バイヤーのみ利用可能
    // TODO: Service接続後、new BuyerProcurementRequestCreateResponse(procurementRequestId)をbodyに入れて201 Createdで返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @GetMapping(BuyerProcurementRequestApi.V1.BY_ID)
  public ResponseEntity<ProcurementRequestDetailResponse> getDetail(
    @PathVariable Long procurementRequestId
  ) {

    // 募集を登録したBuyerアカウントのみ利用可能
    // TODO: Serviceから募集情報詳細を取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @GetMapping(BuyerProcurementRequestApi.V1.BASE)
  public ResponseEntity<BuyerProcurementRequestListResponse> list() {

    // 認証中のBuyerアカウントが登録した募集のみ取得可能
    // TODO: Serviceから募集情報リストを取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @ApiResponse(
    responseCode = "204",
    description = "No Content"
  )
  @PutMapping(BuyerProcurementRequestApi.V1.BY_ID)
  public ResponseEntity<Void> update(
    @PathVariable Long procurementRequestId,
    @Valid @RequestBody BuyerProcurementRequestUpdateRequest request
  ) {

    // 募集を登録したBuyerアカウントのみ利用可能
    // TODO: Serviceで募集情報を更新し、204 No Contentを返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @ApiResponse(
    responseCode = "204",
    description = "No Content"
  )
  @DeleteMapping(BuyerProcurementRequestApi.V1.BY_ID)
  public ResponseEntity<Void> delete(
    @PathVariable Long procurementRequestId
  ) {

    // 募集を登録したBuyerアカウントのみ利用可能
    // TODO: Serviceで登録元Buyerの募集情報を削除し、204 No Contentを返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
