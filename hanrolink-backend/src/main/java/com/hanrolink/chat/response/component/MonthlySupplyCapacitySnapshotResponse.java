package com.hanrolink.chat.response.component;

import java.time.YearMonth;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record MonthlySupplyCapacitySnapshotResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  YearMonth targetMonth,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Integer availableQuantity
) {
  public MonthlySupplyCapacitySnapshotResponse {
    Objects.requireNonNull(
      targetMonth,
      "MonthlySupplyCapacitySnapshotResponse.targetMonth must not be null"
    );

    Objects.requireNonNull(
      availableQuantity,
      "MonthlySupplyCapacitySnapshotResponse.availableQuantity must not be null"
    );
  }
}
