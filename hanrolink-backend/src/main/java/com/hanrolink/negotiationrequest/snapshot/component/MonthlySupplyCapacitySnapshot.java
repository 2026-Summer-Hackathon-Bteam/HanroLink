package com.hanrolink.negotiationrequest.snapshot.component;

import java.time.YearMonth;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

public record MonthlySupplyCapacitySnapshot(
  @JsonFormat(pattern = "yyyy-MM")
  YearMonth targetMonth,
  Integer availableQuantity
) {
  public MonthlySupplyCapacitySnapshot {
    Objects.requireNonNull(
      targetMonth,
      "MonthlySupplyCapacitySnapshot.targetMonth must not be null"
    );

    Objects.requireNonNull(
      availableQuantity,
      "MonthlySupplyCapacitySnapshot.availableQuantity must not be null"
    );
  }
}
