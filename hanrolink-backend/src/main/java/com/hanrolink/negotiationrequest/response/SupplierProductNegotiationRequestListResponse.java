package com.hanrolink.negotiationrequest.response;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long productNegotiationRequestId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductSnapshot product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Buyer buyer,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {

  public record ProductSnapshot(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name
  ) {}

  public record Buyer(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    UUID accountId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String businessName
  ) {}
}
