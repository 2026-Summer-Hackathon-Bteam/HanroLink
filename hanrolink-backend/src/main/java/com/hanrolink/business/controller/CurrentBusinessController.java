package com.hanrolink.business.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.business.api.BusinessApi;
import com.hanrolink.business.response.CurrentBusinessGetResponse;
import com.hanrolink.business.service.CurrentBusinessService;
import com.hanrolink.security.authorization.policy.RequiresApprovedBusiness;

@RestController
public class CurrentBusinessController {

  private final CurrentBusinessService currentBusinessService;

  public CurrentBusinessController(
    CurrentBusinessService currentBusinessService
  ) {
    this.currentBusinessService = currentBusinessService;
  }

  /**
   * 認証済みユーザーに紐づく事業者情報を取得する
   * @param jwt 認証済みユーザーのJWT
   * @return 認証済みユーザーに紐づく事業者情報
   */
  @RequiresApprovedBusiness
  @GetMapping(BusinessApi.V1.MINE)
  public ResponseEntity<CurrentBusinessGetResponse> get(
    @AuthenticationPrincipal Jwt jwt
  ) {

    return ResponseEntity.ok(currentBusinessService.get(jwt.getSubject()));
  }
}
