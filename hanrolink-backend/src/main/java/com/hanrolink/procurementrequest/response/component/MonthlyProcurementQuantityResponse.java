package com.hanrolink.procurementrequest.response.component;

import java.time.YearMonth;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record MonthlyProcurementQuantityResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  YearMonth targetMonth,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Integer desiredQuantity
) {
  public MonthlyProcurementQuantityResponse {
    Objects.requireNonNull(
      targetMonth,
      "MonthlyProcurementQuantityResponse.targetMonth must not be null"
    );

    Objects.requireNonNull(
      desiredQuantity,
      "MonthlyProcurementQuantityResponse.desiredQuantity must not be null"
    );
  }
}
