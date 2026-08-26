package com.hanrolink.negotiationrequest.snapshot;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

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
) {
  public ProcurementRequestSnapshot {
    Objects.requireNonNull(
      sourceUpdatedAt,
      "ProcurementRequestSnapshot.sourceUpdatedAt must not be null"
    );

    Objects.requireNonNull(
      productCategory,
      "ProcurementRequestSnapshot.productCategory must not be null"
    );

    Objects.requireNonNull(
      title,
      "ProcurementRequestSnapshot.title must not be null"
    );

    Objects.requireNonNull(
      description,
      "ProcurementRequestSnapshot.description must not be null"
    );

    if (storageTypes == null
      || storageTypes.isEmpty()
      || storageTypes.stream().anyMatch(Objects::isNull)
    ) {
      throw new IllegalArgumentException(
        "ProcurementRequestSnapshot.storageTypes must be a non-empty list without null elements"
      );
    }
    storageTypes = List.copyOf(storageTypes);

    if (monthlyProcurementQuantities == null
      || monthlyProcurementQuantities.isEmpty()
      || monthlyProcurementQuantities.stream().anyMatch(Objects::isNull)
    ) {
      throw new IllegalArgumentException(
        "ProcurementRequestSnapshot.monthlyProcurementQuantities must be a non-empty list without null elements"
      );
    }
    monthlyProcurementQuantities = List.copyOf(monthlyProcurementQuantities);
  }
}
