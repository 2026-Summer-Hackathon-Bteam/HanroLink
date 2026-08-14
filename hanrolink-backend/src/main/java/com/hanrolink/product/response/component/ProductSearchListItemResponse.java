package com.hanrolink.product.response.component;

import java.util.List;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductSearchListItemResponse(
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
  public ProductSearchListItemResponse {
    Objects.requireNonNull(
      id,
      "ProductSearchListItemResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "ProductSearchListItemResponse.name must not be null"
    );

    Objects.requireNonNull(
      businessName,
      "ProductSearchListItemResponse.businessName must not be null"
    );

    Objects.requireNonNull(
      productCategoryName,
      "ProductSearchListItemResponse.productCategoryName must not be null"
    );

    Objects.requireNonNull(
      mainIngredientRegionName,
      "ProductSearchListItemResponse.mainIngredientRegionName must not be null"
    );

    Objects.requireNonNull(
      monthlySupplyCapacities,
      "ProductSearchListItemResponse.monthlySupplyCapacities must not be null"
    );

    Objects.requireNonNull(
      mainImageUrl,
      "ProductSearchListItemResponse.mainImageUrl must not be null"
    );
  }
}
