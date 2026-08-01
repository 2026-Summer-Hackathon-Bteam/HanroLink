package com.hanrolink.business.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.business.api.BusinessApi;
import com.hanrolink.business.response.CurrentBusinessGetResponse;

@RestController
public class CurrentBusinessController {

  // バイヤー、サプライヤー利用可能
  @GetMapping(BusinessApi.V1.MINE)
  public ResponseEntity<CurrentBusinessGetResponse> get() {

    // TODO: 現在のユーザーに紐づく企業名を取得して、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
