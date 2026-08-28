package com.hanrolink.chat.response;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyChatOverviewResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String channelName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String counterpartyBusinessName
) {
  public MyChatOverviewResponse {
    Objects.requireNonNull(
      channelName,
      "MyChatOverviewResponse.channelName must not be null"
    );

    Objects.requireNonNull(
      counterpartyBusinessName,
      "MyChatOverviewResponse.counterpartyBusinessName must not be null"
    );
  }
}
