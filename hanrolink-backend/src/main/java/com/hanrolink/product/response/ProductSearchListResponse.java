package com.hanrolink.product.response;

import java.util.List;
import java.util.Objects;

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

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  PaginationResponse pagination
) {
  public ProductSearchListResponse {
    Objects.requireNonNull(
      id,
      "ProductSearchListResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "ProductSearchListResponse.name must not be null"
    );

    Objects.requireNonNull(
      businessName,
      "ProductSearchListResponse.businessName must not be null"
    );

    Objects.requireNonNull(
      productCategoryName,
      "ProductSearchListResponse.productCategoryName must not be null"
    );

    Objects.requireNonNull(
      mainIngredientRegionName,
      "ProductSearchListResponse.mainIngredientRegionName must not be null"
    );

    Objects.requireNonNull(
      monthlySupplyCapacities,
      "ProductSearchListResponse.monthlySupplyCapacities must not be null"
    );

    Objects.requireNonNull(
      mainImageUrl,
      "ProductSearchListResponse.mainImageUrl must not be null"
    );

    Objects.requireNonNull(
      pagination,
      "ProductSearchListResponse.pagination must not be null"
    );
  }
}
