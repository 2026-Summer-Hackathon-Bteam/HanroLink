package com.hanrolink.procurementrequest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.procurementrequest.api.ProcurementRequestApi;
import com.hanrolink.procurementrequest.response.BuyerProcurementRequestFormOptionsResponse;
import com.hanrolink.procurementrequest.service.BuyerProcurementRequestFormOptionsService;
import com.hanrolink.security.authorization.policy.RequiresApprovedBuyer;

@RestController
public class BuyerProcurementRequestFormOptionsController {

  private final BuyerProcurementRequestFormOptionsService buyerProcurementRequestFormOptionsService;

  public BuyerProcurementRequestFormOptionsController(
    BuyerProcurementRequestFormOptionsService buyerProcurementRequestFormOptionsService
  ) {
    this.buyerProcurementRequestFormOptionsService = buyerProcurementRequestFormOptionsService;
  }

  /**
   * 募集情報入力フォームで使用する選択肢を取得する
   * @return 募集情報入力フォームの選択肢
   */
  @RequiresApprovedBuyer
  @GetMapping(ProcurementRequestApi.V1.FORM_OPTIONS)
  public ResponseEntity<BuyerProcurementRequestFormOptionsResponse> get() {
    return ResponseEntity.ok(
      buyerProcurementRequestFormOptionsService.get()
    );
  }
}
