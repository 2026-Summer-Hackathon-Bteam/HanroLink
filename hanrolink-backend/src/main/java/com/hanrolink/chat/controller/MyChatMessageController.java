package com.hanrolink.chat.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.chat.api.ChatApi;
import com.hanrolink.chat.request.MyChatMessageCreateRequest;
import com.hanrolink.chat.request.MyChatMessageListRequest;
import com.hanrolink.chat.response.MyChatMessageListResponse;
import com.hanrolink.chat.service.MyChatMessageService;
import com.hanrolink.security.authorization.policy.RequiresApprovedBusiness;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@Profile("s3 & cloudfront")
@RestController
public class MyChatMessageController {

  private final MyChatMessageService myChatMessageService;

  public MyChatMessageController(
    MyChatMessageService myChatMessageService
  ) {
    this.myChatMessageService = myChatMessageService;
  }

  /**
   * 指定されたチャンネルへメッセージの作成を受け付ける
   * @param jwt 認証済みユーザーのJWT
   * @param channelId 作成対象のチャンネル公開識別子
   * @param request メッセージの入力情報
   * @return 作成結果
   */
  @RequiresApprovedBusiness
  @ApiResponse(
    responseCode = "201",
    description = "Created"
  )
  @PostMapping(ChatApi.V1.MESSAGES)
  public ResponseEntity<Void> create(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable UUID channelId,
    @Valid @RequestBody MyChatMessageCreateRequest request
  ) {
    myChatMessageService.create(
      jwt.getSubject(),
      channelId,
      request
    );

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 指定されたチャンネルのメッセージ一覧を返す
   * @param jwt 認証済みユーザーのJWT
   * @param channelId 取得対象のチャンネル公開識別子
   * @param request メッセージ一覧の取得条件
   * @return 添付ファイル情報を含むメッセージ一覧
   */
  @RequiresApprovedBusiness
  @GetMapping(ChatApi.V1.MESSAGES)
  public ResponseEntity<MyChatMessageListResponse> list(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable UUID channelId,
    @Valid
    @ParameterObject
    @ModelAttribute MyChatMessageListRequest request
  ) {
    return ResponseEntity.ok(
      myChatMessageService.list(
        jwt.getSubject(),
        channelId,
        request
      )
    );
  }
}
