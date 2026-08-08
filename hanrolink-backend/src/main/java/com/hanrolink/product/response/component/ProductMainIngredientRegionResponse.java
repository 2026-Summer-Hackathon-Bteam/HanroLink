package com.hanrolink.product.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductMainIngredientRegionResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name
) {
  public ProductMainIngredientRegionResponse {
    Objects.requireNonNull(
      id,
      "ProductMainIngredientRegionResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "ProductMainIngredientRegionResponse.name must not be null"
    );
  }
}
