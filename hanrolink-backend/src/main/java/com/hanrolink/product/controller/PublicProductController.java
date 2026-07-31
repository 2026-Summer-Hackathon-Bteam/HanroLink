package com.hanrolink.product.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.product.response.PublicProductListResponse;

@RestController
public class PublicProductController {

  // 誰でもアクセス可能
  @GetMapping(ProductApi.V1.BASE_PUBLIC)
  public ResponseEntity<List<PublicProductListResponse>> list() {

    // Serviceから商品情報リストを取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
