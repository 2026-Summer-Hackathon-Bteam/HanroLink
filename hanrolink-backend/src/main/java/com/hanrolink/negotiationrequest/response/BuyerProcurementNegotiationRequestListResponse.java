package com.hanrolink.negotiationrequest.response;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long procurementNegotiationRequestId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProcurementRequestSnapshot procurementRequest,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductSnapshot product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {

  public record ProcurementRequestSnapshot(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String title
  ) {}

  public record ProductSnapshot(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String businessName
  ) {}
}
