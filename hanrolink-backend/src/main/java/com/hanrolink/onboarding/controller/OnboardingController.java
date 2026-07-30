package com.hanrolink.onboarding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.onboarding.api.OnboardingApi;
import com.hanrolink.onboarding.response.OnboardingGetResponse;

@RestController
public class OnboardingController {

  // サプライヤー、バイヤーが利用可能
  @GetMapping(OnboardingApi.V1.BASE)
  public ResponseEntity<OnboardingGetResponse> get() {

    // TODO: JWTからemailを取得して、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
