package com.hanrolink.product.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.product.request.SupplierProductImageUploadCreateRequest;
import com.hanrolink.product.response.SupplierProductImageUploadCreateResponse;
import com.hanrolink.product.service.SupplierProductImageUploadService;
import com.hanrolink.security.authorization.policy.RequiresApprovedSupplier;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@Profile("s3")
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
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(ProductApi.V1.IMAGE_UPLOADS)
  public ResponseEntity<SupplierProductImageUploadCreateResponse> create(
    @AuthenticationPrincipal Jwt jwt,
    @Valid @RequestBody SupplierProductImageUploadCreateRequest request
  ) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(
      supplierProductImageUploadService.create(
        jwt.getSubject(),
        request
      )
    );
  }
}
