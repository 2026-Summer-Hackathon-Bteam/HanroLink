package com.hanrolink.productcategory.response.component;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductCategoryResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name
) {}
