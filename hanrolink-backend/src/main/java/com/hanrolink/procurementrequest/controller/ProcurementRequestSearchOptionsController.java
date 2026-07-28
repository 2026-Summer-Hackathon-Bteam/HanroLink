package com.hanrolink.procurementrequest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.procurementrequest.api.ProcurementRequestApi;
import com.hanrolink.procurementrequest.response.ProcurementRequestSearchOptionsResponse;

@RestController
public class ProcurementRequestSearchOptionsController {

  // 管理者、サプライヤー利用可能
  @GetMapping(ProcurementRequestApi.V1.SEARCH_OPTIONS)
  public ResponseEntity<ProcurementRequestSearchOptionsResponse> get() {

    // TODO: Serviceから検索条件を取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
