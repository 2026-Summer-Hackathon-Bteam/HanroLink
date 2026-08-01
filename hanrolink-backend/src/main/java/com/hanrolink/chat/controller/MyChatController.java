package com.hanrolink.chat.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.chat.api.ChatApi;
import com.hanrolink.chat.response.MyChatDetailResponse;
import com.hanrolink.chat.response.MyChatListResponse;

@RestController
public class MyChatController {

  // バイヤー、サプライヤー利用可能
  @GetMapping(ChatApi.V1.MINE)
  public ResponseEntity<List<MyChatListResponse>> list() {

    // TODO: 現在のユーザーに紐づくチャンネル一覧を取得して、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  // 当事者のバイヤー、サプライヤーのみ利用可能
  @GetMapping(ChatApi.V1.BY_ID)
  public ResponseEntity<MyChatDetailResponse> getDetail(
    @PathVariable UUID channelId
  ) {

    // TODO: チャンネルに紐づくチャンネルチャットのヘッダー情報一覧を取得して、ResponseEntity.ok(response)で返す
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
