package com.hanrolink.procurementrequest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.procurementrequest.api.ProcurementRequestApi;
import com.hanrolink.procurementrequest.response.ProcurementRequestSearchOptionsResponse;
import com.hanrolink.procurementrequest.service.ProcurementRequestSearchOptionsService;
import com.hanrolink.security.authorization.policy.RequiresAdminOrApprovedSupplier;

@RestController
public class ProcurementRequestSearchOptionsController {

  private final ProcurementRequestSearchOptionsService procurementRequestSearchOptionsService;

  public ProcurementRequestSearchOptionsController(
    ProcurementRequestSearchOptionsService procurementRequestSearchOptionsService
  ) {
    this.procurementRequestSearchOptionsService = procurementRequestSearchOptionsService;
  }

  /**
   * 募集情報検索フォームで使用する選択肢を取得する
   * @return 募集情報検索フォームの選択肢
   */
  @RequiresAdminOrApprovedSupplier
  @GetMapping(ProcurementRequestApi.V1.SEARCH_OPTIONS)
  public ResponseEntity<ProcurementRequestSearchOptionsResponse> get() {
    return ResponseEntity.ok(
      procurementRequestSearchOptionsService.get()
    );
  }
}
