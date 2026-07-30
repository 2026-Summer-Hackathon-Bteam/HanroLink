package com.hanrolink.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.account.api.AccountApi;
import com.hanrolink.account.response.BuyerProfileGetResponse;

@RestController
public class BuyerAccountController {

  // 管理者、サプライヤー、対象のバイヤー本人が利用可能
  @GetMapping(AccountApi.V1.BUYER)
  public ResponseEntity<BuyerProfileGetResponse> get() {

    // TODO: BUYERロールの担当者アカウントに紐づく事業者プロフィールを取得して、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
