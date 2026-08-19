package com.hanrolink.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.product.response.SupplierProductFormOptionsResponse;
import com.hanrolink.product.service.SupplierProductFormOptionsService;
import com.hanrolink.security.authorization.policy.RequiresApprovedSupplier;

@RestController
public class SupplierProductFormOptionsController {

  private final SupplierProductFormOptionsService supplierProductFormOptionsService;

  public SupplierProductFormOptionsController(
    SupplierProductFormOptionsService supplierProductFormOptionsService
  ) {
    this.supplierProductFormOptionsService = supplierProductFormOptionsService;
  }

  /**
   * 商品情報入力フォームで使用する選択肢を返す
   * @return 商品情報入力フォームの選択肢
   */
  @RequiresApprovedSupplier
  @GetMapping(ProductApi.V1.FORM_OPTIONS)
  public ResponseEntity<SupplierProductFormOptionsResponse> get() {
    return ResponseEntity.ok(
      supplierProductFormOptionsService.get()
    );
  }
}
