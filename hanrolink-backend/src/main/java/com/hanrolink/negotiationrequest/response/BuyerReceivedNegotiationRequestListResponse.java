package com.hanrolink.negotiationrequest.response;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProcurementRequestResponse;
import com.hanrolink.negotiationrequest.response.component.BuyerReceivedNegotiationRequestProductResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerReceivedNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  NegotiationRequestProcurementRequestResponse procurementRequest,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BuyerReceivedNegotiationRequestProductResponse product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {
  public BuyerReceivedNegotiationRequestListResponse {
    Objects.requireNonNull(
      id,
      "BuyerReceivedNegotiationRequestListResponse.id must not be null"
    );

    Objects.requireNonNull(
      procurementRequest,
      "BuyerReceivedNegotiationRequestListResponse.procurementRequest must not be null"
    );

    Objects.requireNonNull(
      product,
      "BuyerReceivedNegotiationRequestListResponse.product must not be null"
    );

    Objects.requireNonNull(
      expiresAt,
      "BuyerReceivedNegotiationRequestListResponse.expiresAt must not be null"
    );
  }
}
