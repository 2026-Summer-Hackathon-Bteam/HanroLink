package com.hanrolink.productcategory.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductCategoryResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name
) {
  public ProductCategoryResponse {
    Objects.requireNonNull(
      id,
      "ProductCategoryResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "ProductCategoryResponse.name must not be null"
    );
  }
}
