package com.hanrolink.negotiationrequest.response;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProductResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerSentNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  NegotiationRequestProductResponse product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {
  public BuyerSentNegotiationRequestListResponse {
    Objects.requireNonNull(
      id,
      "BuyerSentNegotiationRequestListResponse.id must not be null"
    );

    Objects.requireNonNull(
      product,
      "BuyerSentNegotiationRequestListResponse.product must not be null"
    );

    Objects.requireNonNull(
      expiresAt,
      "BuyerSentNegotiationRequestListResponse.expiresAt must not be null"
    );
  }
}
