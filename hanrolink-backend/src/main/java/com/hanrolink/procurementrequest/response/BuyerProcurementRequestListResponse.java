package com.hanrolink.procurementrequest.response;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String title,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant updatedAt
) {}
