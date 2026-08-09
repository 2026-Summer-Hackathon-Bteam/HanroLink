package com.hanrolink.chat.response;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyChatListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant lastActivityAt
) {
  public MyChatListResponse {
    Objects.requireNonNull(
      id,
      "MyChatListResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "MyChatListResponse.name must not be null"
    );

    Objects.requireNonNull(
      lastActivityAt,
      "MyChatListResponse.lastActivityAt must not be null"
    );
  }
}
