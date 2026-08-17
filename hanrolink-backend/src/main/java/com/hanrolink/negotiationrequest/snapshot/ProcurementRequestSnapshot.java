package com.hanrolink.negotiationrequest.snapshot;

import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ProcurementRequestSnapshot(
  Instant sourceUpdatedAt,
  String productCategoryName,
  String title,
  String description,
  String requiredTradeTerms,
  Integer desiredUnitPrice,
  Short deliveryShelfLifeDays,
  List<String> storageTypeNames,
  List<MonthlyProcurementQuantitySnapshot> monthlyProcurementQuantities
) {
  public record MonthlyProcurementQuantitySnapshot(
    @JsonFormat(pattern = "yyyy-MM")
    YearMonth targetMonth,
    Integer desiredQuantity
  ) {}
}
