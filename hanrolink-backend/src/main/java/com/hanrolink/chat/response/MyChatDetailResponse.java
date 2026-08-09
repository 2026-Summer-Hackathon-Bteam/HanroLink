package com.hanrolink.chat.response;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyChatDetailResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String counterpartyBusinessName
) {
  public MyChatDetailResponse {
    Objects.requireNonNull(
      name,
      "MyChatDetailResponse.name must not be null"
    );

    Objects.requireNonNull(
      counterpartyBusinessName,
      "MyChatDetailResponse.counterpartyBusinessName must not be null"
    );
  }
}
