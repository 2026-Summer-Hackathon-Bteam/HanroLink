package com.hanrolink.negotiationrequest.response.component;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record NegotiationRequestSenderBusinessResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name
) {
  public NegotiationRequestSenderBusinessResponse {
    Objects.requireNonNull(
      id,
      "NegotiationRequestSenderBusinessResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "NegotiationRequestSenderBusinessResponse.name must not be null"
    );
  }
}
