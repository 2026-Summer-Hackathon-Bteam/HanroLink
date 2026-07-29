package com.hanrolink.negotiationrequest.response;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProductNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long productNegotiationRequestId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductSnapshot product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {

  public record ProductSnapshot(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name
  ) {}
}
