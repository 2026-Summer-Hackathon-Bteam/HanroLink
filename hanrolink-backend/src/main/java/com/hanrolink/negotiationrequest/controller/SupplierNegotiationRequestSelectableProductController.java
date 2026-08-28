package com.hanrolink.negotiationrequest.controller;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.negotiationrequest.api.SupplierNegotiationRequestApi;
import com.hanrolink.negotiationrequest.response.SupplierNegotiationRequestSelectableProductResponse;
import com.hanrolink.negotiationrequest.service.SupplierNegotiationRequestSelectableProductService;
import com.hanrolink.security.authorization.policy.RequiresApprovedSupplier;

@Profile("cloudfront")
@RestController
public class SupplierNegotiationRequestSelectableProductController {

  private final SupplierNegotiationRequestSelectableProductService supplierNegotiationRequestSelectableProductService;

  public SupplierNegotiationRequestSelectableProductController(
    SupplierNegotiationRequestSelectableProductService supplierNegotiationRequestSelectableProductService
  ) {
    this.supplierNegotiationRequestSelectableProductService = supplierNegotiationRequestSelectableProductService;
  }

  /**
   * 募集への商談希望で選択可能な自社商品一覧を返す
   * @param jwt 認証済みユーザーのJWT
   * @return 選択可能な自社商品一覧
   */
  @RequiresApprovedSupplier
  @GetMapping(SupplierNegotiationRequestApi.V1.SELECTABLE_PRODUCTS)
  public ResponseEntity<List<SupplierNegotiationRequestSelectableProductResponse>> list(
    @AuthenticationPrincipal Jwt jwt
  ) {
    return ResponseEntity.ok(
      supplierNegotiationRequestSelectableProductService.list(jwt.getSubject())
    );
  }
}
