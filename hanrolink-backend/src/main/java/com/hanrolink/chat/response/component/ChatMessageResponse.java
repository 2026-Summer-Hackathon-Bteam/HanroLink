package com.hanrolink.chat.response.component;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatMessageResponse(
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
  public ChatMessageResponse {
    Objects.requireNonNull(
      id,
      "ChatMessageResponse.id must not be null"
    );

    Objects.requireNonNull(
      senderBusinessName,
      "ChatMessageResponse.senderBusinessName must not be null"
    );

    Objects.requireNonNull(
      isMine,
      "ChatMessageResponse.isMine must not be null"
    );

    Objects.requireNonNull(
      createdAt,
      "ChatMessageResponse.createdAt must not be null"
    );
  }
}
