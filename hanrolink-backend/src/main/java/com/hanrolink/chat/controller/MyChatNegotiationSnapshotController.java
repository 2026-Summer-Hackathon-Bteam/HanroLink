package com.hanrolink.chat.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.chat.api.ChatApi;
import com.hanrolink.chat.response.MyChatNegotiationSnapshotResponse;
import com.hanrolink.chat.service.MyChatNegotiationSnapshotService;
import com.hanrolink.security.authorization.policy.RequiresApprovedBusiness;

@RestController
public class MyChatNegotiationSnapshotController {

  private final MyChatNegotiationSnapshotService myChatNegotiationSnapshotService;

  public MyChatNegotiationSnapshotController(
    MyChatNegotiationSnapshotService myChatNegotiationSnapshotService
  ) {
    this.myChatNegotiationSnapshotService = myChatNegotiationSnapshotService;
  }

  /**
   * 指定されたチャンネルの商談希望時点と承諾時点のスナップショットおよび変更項目を返す
   * @param jwt 認証済みユーザーのJWT
   * @param channelId 取得対象のチャンネル公開識別子
   * @return 商談希望時点・承諾時点のスナップショットと変更項目
   */
  @RequiresApprovedBusiness
  @GetMapping(ChatApi.V1.NEGOTIATION_SNAPSHOTS)
  public ResponseEntity<MyChatNegotiationSnapshotResponse> get(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable UUID channelId
  ) {
    return ResponseEntity.ok(
      myChatNegotiationSnapshotService.get(
        jwt.getSubject(),
        channelId
      )
    );
  }
}
