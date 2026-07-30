package com.hanrolink.onboarding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.onboarding.api.OnboardingApi;
import com.hanrolink.onboarding.request.SupplierOnboardingRegisterRequest;
import com.hanrolink.onboarding.response.OnboardingRegisterResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class SupplierOnboardingController {

  // サプライヤーのみ利用可能
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(OnboardingApi.V1.SUPPLIER)
  public ResponseEntity<OnboardingRegisterResponse> register(
    @Valid @RequestBody SupplierOnboardingRegisterRequest request
  ) {

    // TODO: JWTからsubと検証済みemailを取得する
    // TODO: サプライヤーの会社情報、担当者情報登録後、201 Created, PENDINGで返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }  
}
