package com.hanrolink.procurementrequest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.procurementrequest.api.ProcurementRequestApi;
import com.hanrolink.procurementrequest.response.ProcurementRequestDetailResponse;
import com.hanrolink.procurementrequest.response.ProcurementRequestListResponse;

/**
 * Supplier・Admin向けの募集情報閲覧API。
 * Buyerによる自社募集の管理は {@link BuyerProcurementRequestController}で扱う。
 */
@RestController
public class ProcurementRequestReadController {

  // 管理者、サプライヤー利用可能
  @GetMapping(ProcurementRequestApi.V1.BY_ID)
  public ResponseEntity<ProcurementRequestDetailResponse> getDetail(
    @PathVariable Long procurementRequestId
  ) {

    // TODO: Serviceから募集情報詳細を取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  // 管理者、サプライヤー利用可能
  @GetMapping(ProcurementRequestApi.V1.BASE)
  public ResponseEntity<ProcurementRequestListResponse> list() {

    // TODO: Serviceから募集情報リストを取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
