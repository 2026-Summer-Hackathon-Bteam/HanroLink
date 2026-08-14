package com.hanrolink.product.controller;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.product.request.SupplierProductCreateRequest;
import com.hanrolink.product.request.SupplierProductUpdateRequest;
import com.hanrolink.product.request.SupplierProductUpdateVisibilityRequest;
import com.hanrolink.product.response.SupplierProductCreateResponse;
import com.hanrolink.product.response.SupplierProductListResponse;
import com.hanrolink.product.service.SupplierProductManagementService;
import com.hanrolink.security.authorization.policy.RequiresApprovedSupplier;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

/**
 * 認証中のSupplierが自社の商品情報を管理するためのController。
 * 他のSupplierが登録した商品情報は操作対象に含まない。
 */
@Profile("s3")
@RestController
public class SupplierProductManagementController {

  private final SupplierProductManagementService supplierProductManagementService;

  public SupplierProductManagementController(
    SupplierProductManagementService supplierProductManagementService
  ) {
    this.supplierProductManagementService = supplierProductManagementService;
  }

  /**
   * 商品情報を新規作成することを受け付ける
   * @param jwt 認証済みユーザーのJWT
   * @param request 作成に必要な入力データ
   * @return 商品の作成結果
   */
  @RequiresApprovedSupplier
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(ProductApi.V1.BASE)
  public ResponseEntity<SupplierProductCreateResponse> create(
    @AuthenticationPrincipal Jwt jwt,
    @Valid @RequestBody SupplierProductCreateRequest request
  ) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(
        supplierProductManagementService.create(jwt.getSubject(), request)
      );
  }

  /**
   * 自社に紐づく商品一覧を取得する
   * @param jwt 認証済みユーザーのJWT
   * @return 商品一覧
   */
  @RequiresApprovedSupplier
  @GetMapping(ProductApi.V1.MINE)
  public ResponseEntity<List<SupplierProductListResponse>> list(
    @AuthenticationPrincipal Jwt jwt
  ) {
    return ResponseEntity.ok(
      supplierProductManagementService.list(jwt.getSubject())
    );
  }

  /**
   * 商品情報を更新することを受け付ける
   * @param jwt 認証済みユーザーのJWT
   * @param productId 更新対象の商品ID
   * @param request 商品の更新情報
   * @return 更新結果
   */
  @RequiresApprovedSupplier
  @ApiResponse(
    responseCode = "204",
    description = "No Content"
  )
  @PutMapping(ProductApi.V1.BY_ID)
  public ResponseEntity<Void> update(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable Long productId,
    @Valid @RequestBody SupplierProductUpdateRequest request
  ) {
    supplierProductManagementService.update(
      jwt.getSubject(),
      productId,
      request
    );
    return ResponseEntity.noContent().build();
  }

  /**
   * 自社に紐づく商品の表示状態を更新する
   * @param jwt 認証済みユーザーのJWT
   * @param productId 更新対象の商品ID
   * @param request 表示状態の更新情報
   * @return 更新結果
   */
  @RequiresApprovedSupplier
  @ApiResponse(
    responseCode = "204",
    description = "No Content"
  )
  @PatchMapping(ProductApi.V1.VISIBILITY)
  public ResponseEntity<Void> updateVisibility(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable Long productId,
    @Valid @RequestBody SupplierProductUpdateVisibilityRequest request
  ) {
    supplierProductManagementService.updateVisibility(
      jwt.getSubject(),
      productId,
      request
    );
    return ResponseEntity.noContent().build();
  }

  /**
   * 自社に紐づく商品を削除する
   * @param jwt 認証済みユーザーのJWT
   * @param productId 削除対象の商品ID
   * @return 削除結果
   */
  @RequiresApprovedSupplier
  @ApiResponse(
    responseCode = "204",
    description = "No Content"
  )
  @DeleteMapping(ProductApi.V1.BY_ID)
  public ResponseEntity<Void> delete(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable Long productId
  ) {
    supplierProductManagementService.delete(jwt.getSubject(), productId);
    return ResponseEntity.noContent().build();
  }
}
