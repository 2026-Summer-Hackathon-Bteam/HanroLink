package com.hanrolink.negotiationrequest.response.component;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementNegotiationProductSnapshotSummaryResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName
) {}
