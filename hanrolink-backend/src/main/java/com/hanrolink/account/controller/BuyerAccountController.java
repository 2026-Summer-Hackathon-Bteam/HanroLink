package com.hanrolink.account.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.account.api.AccountApi;
import com.hanrolink.account.response.BuyerProfileGetResponse;
import com.hanrolink.account.service.BuyerAccountService;
import com.hanrolink.security.authorization.policy.RequiresAdminOrApprovedBusinessUserAccount;

@RestController
public class BuyerAccountController {

  private final BuyerAccountService buyerAccountService;

  public BuyerAccountController(
    BuyerAccountService buyerAccountService
  ) {
    this.buyerAccountService = buyerAccountService;
  }

  /**
   * 指定されたバイヤーのプロフィール情報を取得する
   * @param jwt 認証済みユーザーのJWT
   * @param businessUserAccountId 取得対象アカウントの公開識別子
   * @return 取得対象のバイヤープロフィール
   */
  @RequiresAdminOrApprovedBusinessUserAccount
  @GetMapping(AccountApi.V1.BUYER)
  public ResponseEntity<BuyerProfileGetResponse> get(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable UUID businessUserAccountId
  ) {
    return ResponseEntity.ok(
      buyerAccountService.get(
        jwt.getSubject(),
        businessUserAccountId
      )
    );
  }
}
