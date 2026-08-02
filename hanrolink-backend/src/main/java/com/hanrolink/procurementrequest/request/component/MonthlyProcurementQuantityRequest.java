package com.hanrolink.procurementrequest.request.component;

import java.time.YearMonth;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MonthlyProcurementQuantityRequest(
  @NotNull
  @FutureOrPresent(message = "当月以降の月を指定してください")
  YearMonth targetMonth,

  @NotNull
  @Positive
  Integer desiredQuantity
) {}
