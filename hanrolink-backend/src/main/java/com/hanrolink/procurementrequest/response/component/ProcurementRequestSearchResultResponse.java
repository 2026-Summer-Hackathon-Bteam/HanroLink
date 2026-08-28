package com.hanrolink.procurementrequest.response.component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestSearchResultResponse(
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
  public ProcurementRequestSearchResultResponse {
    Objects.requireNonNull(
      id,
      "ProcurementRequestSearchResultResponse.id must not be null"
    );

    Objects.requireNonNull(
      title,
      "ProcurementRequestSearchResultResponse.title must not be null"
    );

    Objects.requireNonNull(
      description,
      "ProcurementRequestSearchResultResponse.description must not be null"
    );

    Objects.requireNonNull(
      productCategoryName,
      "ProcurementRequestSearchResultResponse.productCategoryName must not be null"
    );

    Objects.requireNonNull(
      storageTypeLabels,
      "ProcurementRequestSearchResultResponse.storageTypeLabels must not be null"
    );

    Objects.requireNonNull(
      monthlyProcurementQuantities,
      "ProcurementRequestSearchResultResponse.monthlyProcurementQuantities must not be null"
    );

    Objects.requireNonNull(
      buyer,
      "ProcurementRequestSearchResultResponse.buyer must not be null"
    );
  }
}
