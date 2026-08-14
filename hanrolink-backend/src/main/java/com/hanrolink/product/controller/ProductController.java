package com.hanrolink.product.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.product.request.ProductSearchRequest;
import com.hanrolink.product.response.ProductDetailResponse;
import com.hanrolink.product.response.ProductSearchListResponse;
import com.hanrolink.product.service.ProductService;
import com.hanrolink.security.authorization.AuthenticatedAccountRoleResolver;
import com.hanrolink.security.authorization.enums.JwtAccountRole;
import com.hanrolink.security.authorization.policy.RequiresAdminOrApprovedBusiness;

import jakarta.validation.Valid;

/**
 * 商品情報の閲覧API。
 * Supplierによる自社商品の管理は {@link SupplierProductManagementController}で扱う。
 */
@Profile("s3")
@RestController
public class ProductController {

  private final ProductService productService;

  private final AuthenticatedAccountRoleResolver authenticatedAccountRoleResolver;

  public ProductController(
    ProductService productService,
    AuthenticatedAccountRoleResolver authenticatedAccountRoleResolver
  ) {
    this.productService = productService;
    this.authenticatedAccountRoleResolver = authenticatedAccountRoleResolver;
  }

  /**
   * 商品詳細情報を取得する
   * @param jwt 認証済みユーザーのJWT
   * @param productId 取得対象の商品ID
   * @return 商品詳細情報
   */
  @RequiresAdminOrApprovedBusiness
  @GetMapping(ProductApi.V1.BY_ID)
  public ResponseEntity<ProductDetailResponse> getDetail(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable Long productId
  ) {
    JwtAccountRole authenticatedJwtAccountRole =
      authenticatedAccountRoleResolver.resolve(jwt);

    return ResponseEntity.ok(
      productService.getDetail(
        authenticatedJwtAccountRole,
        jwt.getSubject(),
        productId
      )
    );
  }

  /**
   * 指定された条件に基づいて商品一覧を検索する
   * @param request 商品の検索条件
   * @return 商品の検索結果
   */
  @RequiresAdminOrApprovedBusiness
  @GetMapping(ProductApi.V1.BASE)
  public ResponseEntity<ProductSearchListResponse> search(
    @Valid
    @ParameterObject
    @ModelAttribute
    ProductSearchRequest request
  ) {
    return ResponseEntity.ok(
      productService.search(request)
    );
  }
}
