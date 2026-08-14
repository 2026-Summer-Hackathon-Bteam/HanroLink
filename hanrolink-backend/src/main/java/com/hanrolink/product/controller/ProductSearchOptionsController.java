package com.hanrolink.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.product.response.ProductSearchOptionsResponse;
import com.hanrolink.product.service.ProductSearchOptionsService;
import com.hanrolink.security.authorization.policy.RequiresAdminOrApprovedBusinessUserAccount;

@RestController
public class ProductSearchOptionsController {

  private final ProductSearchOptionsService productSearchOptionsService;

  public ProductSearchOptionsController(
    ProductSearchOptionsService productSearchOptionsService
  ) {
    this.productSearchOptionsService = productSearchOptionsService;
  }

  /**
   * 商品検索フォームで使用する選択肢を取得する
   * @return 商品検索フォームの選択肢
   */
  @RequiresAdminOrApprovedBusinessUserAccount
  @GetMapping(ProductApi.V1.SEARCH_OPTIONS)
  public ResponseEntity<ProductSearchOptionsResponse> get() {
    return ResponseEntity.ok(
      productSearchOptionsService.get()
    );
  }
}
