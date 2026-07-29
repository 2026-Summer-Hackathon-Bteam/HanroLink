package com.hanrolink.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.product.response.ProductSearchOptionsResponse;

@RestController
public class ProductSearchOptionsController {

  // 管理者、サプライヤー、バイヤー利用可能
  @GetMapping(ProductApi.V1.SEARCH_OPTIONS)
  public ResponseEntity<ProductSearchOptionsResponse> get() {

    // TODO: Serviceから検索条件を取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
