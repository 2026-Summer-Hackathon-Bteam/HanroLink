package com.hanrolink.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.account.api.AccountApi;
import com.hanrolink.account.response.CurrentAccountResponse;

@RestController
public class CurrentAccountController {

  // 管理者、サプライヤー、バイヤー利用可能
  @GetMapping(AccountApi.V1.ME)
  public ResponseEntity<CurrentAccountResponse> get() {

    // TODO: JWTから現在のアカウントを特定し、Roleと登録状態を返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
