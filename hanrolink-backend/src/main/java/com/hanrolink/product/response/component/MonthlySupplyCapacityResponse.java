package com.hanrolink.product.response.component;

import java.time.YearMonth;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record MonthlySupplyCapacityResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  YearMonth targetMonth,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Integer availableQuantity
) {
  public MonthlySupplyCapacityResponse {
    Objects.requireNonNull(
      targetMonth,
      "MonthlySupplyCapacityResponse.targetMonth must not be null"
    );

    Objects.requireNonNull(
      availableQuantity,
      "MonthlySupplyCapacityResponse.availableQuantity must not be null"
    );
  }
}
