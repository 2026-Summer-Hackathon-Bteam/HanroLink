package com.hanrolink.chat.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyChatDetailResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String counterpartyBusinessName
) {}
