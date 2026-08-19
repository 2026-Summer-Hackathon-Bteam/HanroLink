package com.hanrolink.negotiationrequest.response.component;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementNegotiationProductResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName
) {
  public BuyerProcurementNegotiationProductResponse {
    Objects.requireNonNull(
      id,
      "BuyerProcurementNegotiationProductResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "BuyerProcurementNegotiationProductResponse.name must not be null"
    );

    Objects.requireNonNull(
      businessName,
      "BuyerProcurementNegotiationProductResponse.businessName must not be null"
    );
  }
}
