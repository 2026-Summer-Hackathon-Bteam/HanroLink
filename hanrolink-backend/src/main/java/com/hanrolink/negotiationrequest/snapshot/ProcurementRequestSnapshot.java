package com.hanrolink.negotiationrequest.snapshot;

import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanrolink.product.enums.StorageType;

public record ProcurementRequestSnapshot(
  Instant sourceUpdatedAt,
  ProductCategorySnapshot productCategory,
  String title,
  String description,
  String requiredTradeTerms,
  Integer desiredUnitPrice,
  Short deliveryShelfLifeDays,
  List<StorageTypeSnapshot> storageTypes,
  List<MonthlyProcurementQuantitySnapshot> monthlyProcurementQuantities
) {
  public record ProductCategorySnapshot(
    Short id,
    String name
  ) {}

  public record StorageTypeSnapshot(
    StorageType value,
    String displayName
  ) {}

  public record MonthlyProcurementQuantitySnapshot(
    @JsonFormat(pattern = "yyyy-MM")
    YearMonth targetMonth,
    Integer desiredQuantity
  ) {}
}
