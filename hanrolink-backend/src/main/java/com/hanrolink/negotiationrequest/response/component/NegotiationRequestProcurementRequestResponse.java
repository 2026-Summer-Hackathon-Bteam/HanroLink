package com.hanrolink.negotiationrequest.response.component;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record NegotiationRequestProcurementRequestResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String title
) {
  public NegotiationRequestProcurementRequestResponse {
    Objects.requireNonNull(
      id,
      "NegotiationRequestProcurementRequestResponse.id must not be null"
    );

    Objects.requireNonNull(
      title,
      "NegotiationRequestProcurementRequestResponse.title must not be null"
    );
  }
}