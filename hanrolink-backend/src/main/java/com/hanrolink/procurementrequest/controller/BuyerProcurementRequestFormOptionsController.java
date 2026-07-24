package com.hanrolink.procurementrequest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.procurementrequest.api.BuyerProcurementRequestApi;
import com.hanrolink.procurementrequest.response.BuyerProcurementRequestFormOptionsGetResponse;

@RestController
public class BuyerProcurementRequestFormOptionsController {

  // バイヤーのみ利用可能
  @GetMapping(BuyerProcurementRequestApi.V1.FORM_OPTIONS)
  public ResponseEntity<BuyerProcurementRequestFormOptionsGetResponse> get() {

    // TODO: Serviceからフォーム選択肢を取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
