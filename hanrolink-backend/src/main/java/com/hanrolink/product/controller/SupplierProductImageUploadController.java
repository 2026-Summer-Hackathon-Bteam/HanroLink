package com.hanrolink.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.product.request.SupplierProductImageUploadCreateRequest;
import com.hanrolink.product.response.SupplierProductImageUploadCreateResponse;
import com.hanrolink.product.service.SupplierProductImageUploadService;
import com.hanrolink.security.authorization.policy.RequiresApprovedSupplier;

import jakarta.validation.Valid;

@RestController
public class SupplierProductImageUploadController {

  private final SupplierProductImageUploadService supplierProductImageUploadService;

  public SupplierProductImageUploadController(
    SupplierProductImageUploadService supplierProductImageUploadService
  ) {
    this.supplierProductImageUploadService = supplierProductImageUploadService;
  }

  /**
   * 商品画像のアップロード情報を発行する
   * @param jwt 認証済みユーザーのJWT
   * @param request 商品画像のアップロード情報
   * @return 発行した商品画像のアップロード情報
   */
  @RequiresApprovedSupplier
  @PostMapping(ProductApi.V1.IMAGE_UPLOADS)
  public ResponseEntity<SupplierProductImageUploadCreateResponse> create(
    @AuthenticationPrincipal Jwt jwt,
    @Valid
    @ModelAttribute SupplierProductImageUploadCreateRequest request
  ) {
    return ResponseEntity.ok(
      supplierProductImageUploadService.create(
        jwt.getSubject(),
        request
      )
    );
  }
}
