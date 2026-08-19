package com.hanrolink.negotiationrequest.response;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.hanrolink.negotiationrequest.response.component.ProductSnapshotSummaryResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProductNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductSnapshotSummaryResponse product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {
  public BuyerProductNegotiationRequestListResponse {
    Objects.requireNonNull(
      id,
      "BuyerProductNegotiationRequestListResponse.id must not be null"
    );

    Objects.requireNonNull(
      product,
      "BuyerProductNegotiationRequestListResponse.product must not be null"
    );

    Objects.requireNonNull(
      expiresAt,
      "BuyerProductNegotiationRequestListResponse.expiresAt must not be null"
    );
  }
}
