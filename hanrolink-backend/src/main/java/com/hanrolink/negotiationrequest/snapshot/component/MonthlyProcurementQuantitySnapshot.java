package com.hanrolink.negotiationrequest.snapshot.component;

import java.time.YearMonth;

import com.fasterxml.jackson.annotation.JsonFormat;

public record MonthlyProcurementQuantitySnapshot(
  @JsonFormat(pattern = "yyyy-MM")
  YearMonth targetMonth,
  Integer desiredQuantity
) {}
