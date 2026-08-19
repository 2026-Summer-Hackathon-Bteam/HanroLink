package com.hanrolink.negotiationrequest.response;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProcurementRequestResponse;
import com.hanrolink.negotiationrequest.response.component.BuyerProcurementNegotiationProductResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  NegotiationRequestProcurementRequestResponse procurementRequest,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BuyerProcurementNegotiationProductResponse product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {
  public BuyerProcurementNegotiationRequestListResponse {
    Objects.requireNonNull(
      id,
      "BuyerProcurementNegotiationRequestListResponse.id must not be null"
    );

    Objects.requireNonNull(
      procurementRequest,
      "BuyerProcurementNegotiationRequestListResponse.procurementRequest must not be null"
    );

    Objects.requireNonNull(
      product,
      "BuyerProcurementNegotiationRequestListResponse.product must not be null"
    );

    Objects.requireNonNull(
      expiresAt,
      "BuyerProcurementNegotiationRequestListResponse.expiresAt must not be null"
    );
  }
}
