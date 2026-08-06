package com.hanrolink.onboarding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.account.enums.BusinessUserAccountRegistrationApiStatus;
import com.hanrolink.onboarding.api.OnboardingApi;
import com.hanrolink.onboarding.request.OnboardingCreateRequest;
import com.hanrolink.onboarding.response.OnboardingGetResponse;
import com.hanrolink.onboarding.service.OnboardingService;
import com.hanrolink.security.authorization.policy.RequiresUnregisteredBusinessUserAccount;
import com.hanrolink.onboarding.response.OnboardingCreateResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class OnboardingController {

  private final OnboardingService onboardingService;

  OnboardingController(
    OnboardingService onboardingService
  ) {
    this.onboardingService = onboardingService;
  }

  /**
   * 初期登録に必要な情報を取得する
   * @param jwt 認証済みユーザーのJWT
   * @return 初期登録に必要な情報
   */
  @RequiresUnregisteredBusinessUserAccount
  @GetMapping(OnboardingApi.V1.BASE)
  public ResponseEntity<OnboardingGetResponse> get(
    @AuthenticationPrincipal Jwt jwt
  ) {
    return ResponseEntity.ok(
      new OnboardingGetResponse(jwt.getClaimAsString("email"))
    );
  }

  /**
   * 初期登録情報の新規作成を受け付ける
   * @param jwt 認証済みユーザーのJWT
   * @param request 作成に必要な入力データ
   * @return 作成処理の結果
   */
  @RequiresUnregisteredBusinessUserAccount
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(OnboardingApi.V1.BASE)
  public ResponseEntity<OnboardingCreateResponse> create(
    @AuthenticationPrincipal Jwt jwt,
    @Valid @RequestBody OnboardingCreateRequest request
  ) {
    onboardingService.create(jwt.getSubject(), jwt.getClaimAsString("email"), request);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(
        new OnboardingCreateResponse(
          BusinessUserAccountRegistrationApiStatus.PENDING
        )
      );
  }
}
