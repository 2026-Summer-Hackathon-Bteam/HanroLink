package com.hanrolink.negotiationrequest.response.component;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerReceivedNegotiationRequestProductResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName
) {
  public BuyerReceivedNegotiationRequestProductResponse {
    Objects.requireNonNull(
      id,
      "BuyerReceivedNegotiationRequestProductResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "BuyerReceivedNegotiationRequestProductResponse.name must not be null"
    );

    Objects.requireNonNull(
      businessName,
      "BuyerReceivedNegotiationRequestProductResponse.businessName must not be null"
    );
  }
}
