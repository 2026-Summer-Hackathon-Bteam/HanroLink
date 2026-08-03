package com.hanrolink.negotiationrequest.response.component;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record NegotiationRequestChannelResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String counterpartyBusinessName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant lastActivityAt
) {}
