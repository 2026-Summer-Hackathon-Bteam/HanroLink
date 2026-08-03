package com.hanrolink.product.request.component;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MonthlySupplyCapacityRequest(
  @NotNull
  @FutureOrPresent(message = "当月以降の月を指定してください")
  @DateTimeFormat(pattern = "yyyy-MM")
  YearMonth targetMonth,

  @NotNull
  @Positive
  Integer availableQuantity
) {}
