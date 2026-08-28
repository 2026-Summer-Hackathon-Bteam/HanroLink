package com.hanrolink.negotiationrequest.response.component;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record NegotiationRequestProductResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name
) {
  public NegotiationRequestProductResponse {
    Objects.requireNonNull(
      id,
      "NegotiationRequestProductResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "NegotiationRequestProductResponse.name must not be null"
    );
  }
}
