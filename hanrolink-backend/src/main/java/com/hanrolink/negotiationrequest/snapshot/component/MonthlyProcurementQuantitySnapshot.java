package com.hanrolink.negotiationrequest.snapshot.component;

import java.time.YearMonth;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

public record MonthlyProcurementQuantitySnapshot(
  @JsonFormat(pattern = "yyyy-MM")
  YearMonth targetMonth,
  Integer desiredQuantity
) {
  public MonthlyProcurementQuantitySnapshot {
    Objects.requireNonNull(
      targetMonth,
      "MonthlyProcurementQuantitySnapshot.targetMonth must not be null"
    );

    Objects.requireNonNull(
      desiredQuantity,
      "MonthlyProcurementQuantitySnapshot.desiredQuantity must not be null"
    );
  }
}
