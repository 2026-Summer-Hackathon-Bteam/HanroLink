package com.hanrolink.productcategory.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductCategoryOptionResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short productCategoryGroupId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name
) {
  public ProductCategoryOptionResponse {
    Objects.requireNonNull(
      id,
      "ProductCategoryOptionResponse.id must not be null"
    );

    Objects.requireNonNull(
      productCategoryGroupId,
      "ProductCategoryOptionResponse.productCategoryGroupId must not be null"
    );

    Objects.requireNonNull(
      name,
      "ProductCategoryOptionResponse.name must not be null"
    );
  }
}
