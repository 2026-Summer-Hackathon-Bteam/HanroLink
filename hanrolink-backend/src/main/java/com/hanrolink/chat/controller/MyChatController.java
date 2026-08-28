package com.hanrolink.chat.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.chat.api.ChatApi;
import com.hanrolink.chat.response.MyChatOverviewResponse;
import com.hanrolink.chat.service.MyChatService;
import com.hanrolink.security.authorization.policy.RequiresApprovedBusiness;
import com.hanrolink.chat.response.MyChatListResponse;

@RestController
public class MyChatController {

  private final MyChatService myChatService;

  public MyChatController(
    MyChatService myChatService
  ) {
    this.myChatService = myChatService;
  }

  /**
   * 自身に紐づくチャット一覧を返す
   * @param jwt 認証済みユーザーのJWT
   * @return チャット一覧
   */
  @RequiresApprovedBusiness
  @GetMapping(ChatApi.V1.MINE)
  public ResponseEntity<List<MyChatListResponse>> list(
    @AuthenticationPrincipal Jwt jwt
  ) {
    return ResponseEntity.ok(
      myChatService.list(jwt.getSubject())
    );
  }

  /**
   * 自身が当事者であるチャットの概要情報を返す
   * @param jwt 認証済みユーザーのJWT
   * @param channelId 取得対象のチャンネル公開識別子
   * @return チャットの概要情報
   */
  @RequiresApprovedBusiness
  @GetMapping(ChatApi.V1.BY_ID)
  public ResponseEntity<MyChatOverviewResponse> getOverview(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable UUID channelId
  ) {
    return ResponseEntity.ok(
      myChatService.getOverview(
        jwt.getSubject(),
        channelId
      )
    );
  }
}
