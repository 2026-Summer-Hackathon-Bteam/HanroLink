package com.hanrolink.chat.response;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyChatListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant lastActivityAt
) {}
