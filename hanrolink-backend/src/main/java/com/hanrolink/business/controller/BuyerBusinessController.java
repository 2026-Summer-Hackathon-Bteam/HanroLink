package com.hanrolink.business.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.business.api.BusinessApi;
import com.hanrolink.business.response.BuyerProfileGetResponse;
import com.hanrolink.business.service.BuyerBusinessService;
import com.hanrolink.security.authorization.AuthenticatedAccountRoleResolver;
import com.hanrolink.security.authorization.enums.JwtAccountRole;
import com.hanrolink.security.authorization.policy.RequiresAdminOrApprovedBusiness;

@RestController
public class BuyerBusinessController {

  private final BuyerBusinessService buyerBusinessService;

  private final AuthenticatedAccountRoleResolver authenticatedAccountRoleResolver;

  public BuyerBusinessController(
    BuyerBusinessService buyerBusinessService,
    AuthenticatedAccountRoleResolver authenticatedAccountRoleResolver
  ) {
    this.buyerBusinessService = buyerBusinessService;
    this.authenticatedAccountRoleResolver = authenticatedAccountRoleResolver;
  }

  /**
   * 指定されたバイヤーのプロフィール情報を返す
   * @param jwt 認証済みユーザーのJWT
   * @param businessId 取得対象事業者の公開識別子
   * @return 取得対象のバイヤープロフィール
   */
  @RequiresAdminOrApprovedBusiness
  @GetMapping(BusinessApi.V1.BUYER)
  public ResponseEntity<BuyerProfileGetResponse> get(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable UUID businessId
  ) {
    JwtAccountRole authenticatedJwtAccountRole = authenticatedAccountRoleResolver.resolve(jwt);

    return ResponseEntity.ok(
      buyerBusinessService.get(
        authenticatedJwtAccountRole,
        jwt.getSubject(),
        businessId
      )
    );
  }
}
