package com.hanrolink.productcategory.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductCategoryGroupOptionResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name
) {
  public ProductCategoryGroupOptionResponse {
    Objects.requireNonNull(
      id,
      "ProductCategoryGroupOptionResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "ProductCategoryGroupOptionResponse.name must not be null"
    );
  }
}
