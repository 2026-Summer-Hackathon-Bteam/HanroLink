package com.hanrolink.product.response.component;

import java.util.List;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductSearchResultResponse(
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
  String mainImageUrl
) {
  public ProductSearchResultResponse {
    Objects.requireNonNull(
      id,
      "ProductSearchResultResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "ProductSearchResultResponse.name must not be null"
    );

    Objects.requireNonNull(
      businessName,
      "ProductSearchResultResponse.businessName must not be null"
    );

    Objects.requireNonNull(
      productCategoryName,
      "ProductSearchResultResponse.productCategoryName must not be null"
    );

    Objects.requireNonNull(
      mainIngredientRegionName,
      "ProductSearchResultResponse.mainIngredientRegionName must not be null"
    );

    Objects.requireNonNull(
      monthlySupplyCapacities,
      "ProductSearchResultResponse.monthlySupplyCapacities must not be null"
    );

    Objects.requireNonNull(
      mainImageUrl,
      "ProductSearchResultResponse.mainImageUrl must not be null"
    );
  }
}
