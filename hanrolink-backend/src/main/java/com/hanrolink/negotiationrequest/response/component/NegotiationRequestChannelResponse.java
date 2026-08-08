package com.hanrolink.negotiationrequest.response.component;

import java.time.Instant;
import java.util.Objects;
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
) {
  public NegotiationRequestChannelResponse {
    Objects.requireNonNull(
      id,
      "NegotiationRequestChannelResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "NegotiationRequestChannelResponse.name must not be null"
    );

    Objects.requireNonNull(
      counterpartyBusinessName,
      "NegotiationRequestChannelResponse.counterpartyBusinessName must not be null"
    );

    Objects.requireNonNull(
      lastActivityAt,
      "NegotiationRequestChannelResponse.lastActivityAt must not be null"
    );
  }
}
