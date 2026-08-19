package com.hanrolink.negotiationrequest.response.component;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementNegotiationProductSnapshotSummaryResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName
) {
  public BuyerProcurementNegotiationProductSnapshotSummaryResponse {
    Objects.requireNonNull(
      id,
      "BuyerProcurementNegotiationProductSnapshotSummaryResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "BuyerProcurementNegotiationProductSnapshotSummaryResponse.name must not be null"
    );

    Objects.requireNonNull(
      businessName,
      "BuyerProcurementNegotiationProductSnapshotSummaryResponse.businessName must not be null"
    );
  }
}
