package com.hanrolink.chat.controller;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.chat.api.ChatApi;
import com.hanrolink.chat.request.MyChatFileUploadCreateRequest;
import com.hanrolink.chat.response.MyChatFileUploadCreateResponse;
import com.hanrolink.chat.service.MyChatFileUploadService;
import com.hanrolink.security.authorization.policy.RequiresApprovedBusiness;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@Profile("s3")
@RestController
public class MyChatFileUploadController {

  private final MyChatFileUploadService myChatFileUploadService;

  public MyChatFileUploadController(
    MyChatFileUploadService myChatFileUploadService
  ) {
    this.myChatFileUploadService = myChatFileUploadService;
  }

  /**
   * チャット添付ファイルをS3へ直接アップロードするための情報の発行を受け付ける
   * @param jwt 認証済みユーザーのJWT
   * @param channelId 対象チャンネルの公開識別子
   * @param request アップロード対象ファイルの情報
   * @return S3への直接アップロードに使用する情報
   */
  @RequiresApprovedBusiness
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(ChatApi.V1.FILE_UPLOADS)
  public ResponseEntity<MyChatFileUploadCreateResponse> create(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable UUID channelId,
    @Valid @RequestBody MyChatFileUploadCreateRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(
      myChatFileUploadService.create(
        jwt.getSubject(),
        channelId,
        request
      )
    );
  }
}
