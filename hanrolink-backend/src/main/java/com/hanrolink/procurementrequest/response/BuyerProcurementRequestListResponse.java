package com.hanrolink.procurementrequest.response;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String title,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant updatedAt
) {
  public BuyerProcurementRequestListResponse {
    Objects.requireNonNull(
      id,
      "BuyerProcurementRequestListResponse.id must not be null"
    );

    Objects.requireNonNull(
      title,
      "BuyerProcurementRequestListResponse.title must not be null"
    );

    Objects.requireNonNull(
      updatedAt,
      "BuyerProcurementRequestListResponse.updatedAt must not be null"
    );
  }
}
