package com.hanrolink.chat.response;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

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
) {
  public MyChatMessageListResponse {
    Objects.requireNonNull(
      id,
      "MyChatMessageListResponse.id must not be null"
    );

    Objects.requireNonNull(
      senderBusinessName,
      "MyChatMessageListResponse.senderBusinessName must not be null"
    );

    Objects.requireNonNull(
      isMine,
      "MyChatMessageListResponse.isMine must not be null"
    );

    Objects.requireNonNull(
      createdAt,
      "MyChatMessageListResponse.createdAt must not be null"
    );
  }
}
