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
import com.hanrolink.security.authorization.policy.RequiresAdminOrApprovedBusiness;

@RestController
public class BuyerBusinessController {

  private final BuyerBusinessService buyerBusinessService;

  public BuyerBusinessController(
    BuyerBusinessService buyerBusinessService
  ) {
    this.buyerBusinessService = buyerBusinessService;
  }

  /**
   * 指定されたバイヤーのプロフィール情報を取得する
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
    return ResponseEntity.ok(
      buyerBusinessService.get(
        jwt.getSubject(),
        businessId
      )
    );
  }
}
