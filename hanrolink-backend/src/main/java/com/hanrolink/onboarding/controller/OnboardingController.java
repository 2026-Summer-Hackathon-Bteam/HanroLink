package com.hanrolink.onboarding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.onboarding.api.OnboardingApi;
import com.hanrolink.onboarding.request.OnboardingCreateRequest;
import com.hanrolink.onboarding.response.OnboardingGetResponse;
import com.hanrolink.onboarding.response.OnboardingCreateResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class OnboardingController {

  // サプライヤー、バイヤーが利用可能
  @GetMapping(OnboardingApi.V1.BASE)
  public ResponseEntity<OnboardingGetResponse> get() {

    // TODO: JWTからemailを取得して、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  // バイヤー、サプライヤー利用可能
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(OnboardingApi.V1.BASE)
  public ResponseEntity<OnboardingCreateResponse> create(
    @Valid @RequestBody OnboardingCreateRequest request
  ) {

    // TODO: JWTからsubと検証済みemailを取得する
    // TODO: Facadeを介し、roleに応じてBuyerまたはSupplierのOnboardingServiceへ処理を振り分ける
    // TODO: 会社情報、担当者情報登録後、201 Created, PENDINGで返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
