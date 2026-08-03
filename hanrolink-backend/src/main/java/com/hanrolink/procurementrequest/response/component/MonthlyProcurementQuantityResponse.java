package com.hanrolink.procurementrequest.response.component;

import java.time.YearMonth;

import io.swagger.v3.oas.annotations.media.Schema;

public record MonthlyProcurementQuantityResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  YearMonth targetMonth,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Integer desiredQuantity
) {}
