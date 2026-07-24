package com.hanrolink.procurementrequest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.procurementrequest.api.SupplierProcurementRequestApi;
import com.hanrolink.procurementrequest.response.ProcurementRequestDetailResponse;
import com.hanrolink.procurementrequest.response.SupplierProcurementRequestListResponse;

@RestController
public class SupplierProcurementRequestController {

  @GetMapping(SupplierProcurementRequestApi.V1.BY_ID)
  public ResponseEntity<ProcurementRequestDetailResponse> getDetail(
    @PathVariable Long procurementRequestId
  ) {

    // サプライヤーのみ利用可能
    // TODO: Serviceから募集情報詳細を取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @GetMapping(SupplierProcurementRequestApi.V1.BASE)
  public ResponseEntity<SupplierProcurementRequestListResponse> list() {

    // サプライヤーのみ利用可能
    // TODO: Serviceから募集情報リストを取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
