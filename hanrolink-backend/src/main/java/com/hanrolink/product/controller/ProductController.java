package com.hanrolink.product.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.product.request.ProductSearchRequest;
import com.hanrolink.product.response.ProductListResponse;

import jakarta.validation.Valid;

/**
 * 商品情報の閲覧API。
 * Supplierによる自社商品の管理は {@link SupplierProductManagementController}で扱う。
 */
@RestController
public class ProductController {

  // 管理者、サプライヤー、バイヤー利用可能
  @GetMapping(ProductApi.V1.BY_ID)
  public ResponseEntity<ProductListResponse> getDetail(
    @PathVariable Long productId
  ) {

    // TODO: Serviceから商品情報詳細を取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  // 管理者、サプライヤー、バイヤー利用可能
  @GetMapping(ProductApi.V1.BASE)
  public ResponseEntity<ProductListResponse> search(
    @Valid
    @ParameterObject
    @ModelAttribute
    ProductSearchRequest request
  ) {

    // TODO: Serviceから商品情報リストを取得し、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
