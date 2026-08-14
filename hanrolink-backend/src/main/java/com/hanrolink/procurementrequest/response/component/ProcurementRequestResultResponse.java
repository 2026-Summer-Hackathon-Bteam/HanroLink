package com.hanrolink.procurementrequest.response.component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestResultResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String title,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String description,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String productCategoryName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<String> storageTypeLabels,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<MonthlyProcurementQuantityResponse> monthlyProcurementQuantities,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProcurementRequestBuyerResponse buyer
) {
  public ProcurementRequestResultResponse {
    Objects.requireNonNull(
      id,
      "ProcurementRequestResultResponse.id must not be null"
    );

    Objects.requireNonNull(
      title,
      "ProcurementRequestResultResponse.title must not be null"
    );

    Objects.requireNonNull(
      description,
      "ProcurementRequestResultResponse.description must not be null"
    );

    Objects.requireNonNull(
      productCategoryName,
      "ProcurementRequestResultResponse.productCategoryName must not be null"
    );

    Objects.requireNonNull(
      storageTypeLabels,
      "ProcurementRequestResultResponse.storageTypeLabels must not be null"
    );

    Objects.requireNonNull(
      monthlyProcurementQuantities,
      "ProcurementRequestResultResponse.monthlyProcurementQuantities must not be null"
    );

    Objects.requireNonNull(
      buyer,
      "ProcurementRequestResultResponse.buyer must not be null"
    );
  }
}
