package com.hanrolink.product.response;

import java.util.List;

import com.hanrolink.pagination.response.component.PaginationResponse;
import com.hanrolink.product.response.component.MonthlySupplyCapacityResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductSearchListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String productCategoryName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String mainIngredientRegionName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<MonthlySupplyCapacityResponse> monthlySupplyCapacities,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String mainImageUrl,

  PaginationResponse pagination
) {}
