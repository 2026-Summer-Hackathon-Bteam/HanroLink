package com.hanrolink.negotiationrequest.snapshot;

import java.time.Instant;
import java.util.List;

import com.hanrolink.negotiationrequest.snapshot.component.MonthlyProcurementQuantitySnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.ProductCategorySnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.StorageTypeSnapshot;

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
) {}
