package com.hanrolink.chat.controller;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.chat.api.ChatApi;
import com.hanrolink.chat.service.MyChatFileAccessService;
import com.hanrolink.infrastructure.cloudfront.CloudFrontSignedCookieValues;
import com.hanrolink.security.authorization.policy.RequiresApprovedBusiness;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Profile("cloudfront")
@RestController
public class MyChatFileAccessController {

  private final MyChatFileAccessService myChatFileAccessService;

  private final String cookieDomain;

  public MyChatFileAccessController(
    MyChatFileAccessService myChatFileAccessService,
    @Value("${app.storage.cloudfront.cookie-domain}")
    String cookieDomain
  ) {
    this.myChatFileAccessService = myChatFileAccessService;
    this.cookieDomain = cookieDomain;
  }

  /**
   * 指定されたチャンネルの添付ファイルを閲覧するための署名付きCookie情報を発行する
   * @param jwt 認証済みユーザーのJWT
   * @param channelId 閲覧対象のチャンネル公開識別子
   * @return CloudFrontの署名付きCookie情報
   */
  @RequiresApprovedBusiness
  @ApiResponse(
    responseCode = "204",
    description = "No Content"
  )
  @PostMapping(ChatApi.V1.FILE_ACCESS)
  public ResponseEntity<Void> create(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable UUID channelId
  ) {
    CloudFrontSignedCookieValues values =
      myChatFileAccessService.create(
        jwt.getSubject(),
        channelId
      );
    ResponseCookie policyCookie = createCookie(
      "CloudFront-Policy",
      extractCookieValue(values.policyHeaderValue()),
      values.cookiePath(),
      values.validDuration()
    );

    ResponseCookie signatureCookie = createCookie(
      "CloudFront-Signature",
      extractCookieValue(values.signatureHeaderValue()),
      values.cookiePath(),
      values.validDuration()
    );

    ResponseCookie keyPairIdCookie = createCookie(
      "CloudFront-Key-Pair-Id",
      extractCookieValue(values.keyPairIdHeaderValue()),
      values.cookiePath(),
      values.validDuration()
    );

    return ResponseEntity
      .noContent()
      .header(
        HttpHeaders.SET_COOKIE,
        policyCookie.toString(),
        signatureCookie.toString(),
        keyPairIdCookie.toString()
      )
      .build();
  }

  private ResponseCookie createCookie(
    String name,
    String value,
    String path,
    Duration validDuration
  ) {
    return ResponseCookie
      .from(name, value)
      .domain(cookieDomain)
      .path(path)
      .maxAge(validDuration)
      .secure(true)
      .httpOnly(true)
      .sameSite("Lax")
      .build();
  }

  private String extractCookieValue(
    String headerValue
  ) {
    return headerValue.substring(
      headerValue.indexOf('=') + 1
    );
  }
}
