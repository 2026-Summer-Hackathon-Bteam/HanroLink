package com.hanrolink.procurementrequest.response;

import java.util.List;
import java.util.Objects;

import com.hanrolink.pagination.response.component.PaginationResponse;
import com.hanrolink.procurementrequest.response.component.MonthlyProcurementQuantityResponse;
import com.hanrolink.procurementrequest.response.component.ProcurementRequestBuyerResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

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
  ProcurementRequestBuyerResponse buyer,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  PaginationResponse pagination
) {
  public ProcurementRequestListResponse {
    Objects.requireNonNull(
      id,
      "ProcurementRequestListResponse.id must not be null"
    );

    Objects.requireNonNull(
      title,
      "ProcurementRequestListResponse.title must not be null"
    );

    Objects.requireNonNull(
      description,
      "ProcurementRequestListResponse.description must not be null"
    );

    Objects.requireNonNull(
      productCategoryName,
      "ProcurementRequestListResponse.productCategoryName must not be null"
    );

    Objects.requireNonNull(
      storageTypeLabels,
      "ProcurementRequestListResponse.storageTypeLabels must not be null"
    );

    Objects.requireNonNull(
      monthlyProcurementQuantities,
      "ProcurementRequestListResponse.monthlyProcurementQuantities must not be null"
    );

    Objects.requireNonNull(
      buyer,
      "ProcurementRequestListResponse.buyer must not be null"
    );

    Objects.requireNonNull(
      pagination,
      "ProcurementRequestListResponse.pagination must not be null"
    );
  }
}
