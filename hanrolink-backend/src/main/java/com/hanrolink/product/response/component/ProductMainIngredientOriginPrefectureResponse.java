package com.hanrolink.product.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductMainIngredientOriginPrefectureResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name
) {
  public ProductMainIngredientOriginPrefectureResponse {
    Objects.requireNonNull(
      id,
      "ProductMainIngredientOriginPrefectureResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "ProductMainIngredientOriginPrefectureResponse.name must not be null"
    );
  }
}
