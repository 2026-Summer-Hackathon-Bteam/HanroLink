package com.hanrolink.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.product.response.SupplierProductFormOptionsResponse;

@RestController
public class SupplierProductFormOptionsController {

  // サプライヤーのみ利用可能
  @GetMapping(ProductApi.V1.FORM_OPTIONS)
  public ResponseEntity<SupplierProductFormOptionsResponse> get() {

    // TODO: Serviceからフォーム選択肢を取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
