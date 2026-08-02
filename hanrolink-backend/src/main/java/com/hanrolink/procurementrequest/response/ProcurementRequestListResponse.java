package com.hanrolink.procurementrequest.response;

import java.util.List;

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
) {}
