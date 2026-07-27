package com.hanrolink.product.response;

import java.time.YearMonth;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductListResponse(
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
  List<MonthlySupplyCapacity> monthlySupplyCapacities,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String mainImageUrl,

  Pagination pagination
) {

  public record MonthlySupplyCapacity(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    YearMonth targetMonth,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer availableQuantity
  ) {}

  public record Pagination(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer page,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer pageSize,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long totalCount,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer totalPages
  ) {}
}
