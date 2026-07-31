package com.hanrolink.negotiationrequest.response;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record NegotiationRequestAcceptResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Channel channel
) {

  public record Channel(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    UUID id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Instant lastActivityAt
  ) {}
}
