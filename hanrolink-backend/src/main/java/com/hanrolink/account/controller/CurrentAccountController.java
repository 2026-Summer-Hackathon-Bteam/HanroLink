package com.hanrolink.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.account.api.AccountApi;
import com.hanrolink.account.enums.JwtAccountRole;
import com.hanrolink.account.response.CurrentAccountResponse;
import com.hanrolink.account.service.CurrentAccountService;
import com.hanrolink.security.authorization.AuthenticatedAccountRoleResolver;

@RestController
public class CurrentAccountController {

  private final CurrentAccountService currentAccountService;

  private final AuthenticatedAccountRoleResolver authenticatedAccountRoleResolver;

  public CurrentAccountController(
    CurrentAccountService currentAccountService,
    AuthenticatedAccountRoleResolver authenticatedAccountRoleResolver
  ) {
    this.currentAccountService = currentAccountService;
    this.authenticatedAccountRoleResolver = authenticatedAccountRoleResolver;
  }

  /**
   * 認証済みユーザーのアカウント状態を取得する
   * @param jwt 認証済みユーザーのJWT
   * @return 現在のアカウント情報
   */
  @GetMapping(AccountApi.V1.ME)
  public ResponseEntity<CurrentAccountResponse> get(
    @AuthenticationPrincipal Jwt jwt
  ) {
    JwtAccountRole authenticatedAccountRole = authenticatedAccountRoleResolver.resolve(jwt);

    return ResponseEntity.ok(currentAccountService.get(
      authenticatedAccountRole,
      jwt.getSubject()
    ));
  }
}
