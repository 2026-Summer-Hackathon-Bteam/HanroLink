package com.hanrolink.chat.response;

import java.time.Instant;
import java.util.List;

import com.hanrolink.chat.response.component.ChatMessageFileResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyChatMessageListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String senderBusinessName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean isMine,

  String body,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant createdAt,

  List<ChatMessageFileResponse> messageFiles
) {}
