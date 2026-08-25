package com.hanrolink.chat.response.component;

import java.util.List;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestSnapshotResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String productCategoryName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String title,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String description,

  String requiredTradeTerms,

  Integer desiredUnitPrice,

  Short deliveryShelfLifeDays,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<String> storageTypeNames,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<MonthlyProcurementQuantitySnapshotResponse> monthlyProcurementQuantities
) {
  public ProcurementRequestSnapshotResponse {
    Objects.requireNonNull(
      productCategoryName,
      "ProcurementRequestSnapshotResponse.productCategoryName must not be null"
    );

    Objects.requireNonNull(
      title,
      "ProcurementRequestSnapshotResponse.title must not be null"
    );

    Objects.requireNonNull(
      description,
      "ProcurementRequestSnapshotResponse.description must not be null"
    );

    Objects.requireNonNull(
      storageTypeNames,
      "ProcurementRequestSnapshotResponse.storageTypeNames must not be null"
    );

    Objects.requireNonNull(
      monthlyProcurementQuantities,
      "ProcurementRequestSnapshotResponse.monthlyProcurementQuantities must not be null"
    );
  }
}
