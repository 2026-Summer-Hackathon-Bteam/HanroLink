package com.hanrolink.product.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

/**
 * 認証中のSupplierが自身の商品情報を管理するためのController。
 * 他のSupplierが登録した商品情報は操作対象に含まない。
 */
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
  @Operation(
    requestBody =
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
          mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
          schema = @Schema(
            implementation = SupplierProductCreateRequest.class
          )
        )
      )
  )
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(ProductApi.V1.BASE)
  public ResponseEntity<SupplierProductCreateResponse> create(
    @AuthenticationPrincipal Jwt jwt,
    @Parameter(hidden = true)
    @Valid
    @ModelAttribute SupplierProductCreateRequest request
  ) {
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(
        supplierProductManagementService.create(jwt.getSubject(), request)
      );
  }

  /**
   * 自身に紐づく商品一覧を取得する
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

  // 商品を登録したSupplierアカウントのみ利用可能
  @Operation(
    requestBody =
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
          mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
          schema = @Schema(
            implementation = SupplierProductUpdateRequest.class
          )
        )
      )
  )
  @ApiResponse(
    responseCode = "204",
    description = "No Content"
  )
  @PutMapping(ProductApi.V1.BY_ID)
  public ResponseEntity<Void> update(
    @PathVariable Long productId,
    @Parameter(hidden = true)
    @Valid
    @ModelAttribute SupplierProductUpdateRequest request
  ) {

    // TODO: Serviceで商品情報を更新し、204 No Contentを返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  /**
   * 自身に紐づく商品の表示状態を更新する
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
   * 自身に紐づく商品を削除する
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
