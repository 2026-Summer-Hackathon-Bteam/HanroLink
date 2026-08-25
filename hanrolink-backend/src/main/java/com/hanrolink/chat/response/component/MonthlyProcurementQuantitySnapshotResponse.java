package com.hanrolink.chat.response.component;

import java.time.YearMonth;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record MonthlyProcurementQuantitySnapshotResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  YearMonth targetMonth,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Integer desiredQuantity
) {
  public MonthlyProcurementQuantitySnapshotResponse {
    Objects.requireNonNull(
      targetMonth,
      "MonthlyProcurementQuantitySnapshotResponse.targetMonth must not be null"
    );

    Objects.requireNonNull(
      desiredQuantity,
      "MonthlyProcurementQuantitySnapshotResponse.desiredQuantity must not be null"
    );
  }
}
