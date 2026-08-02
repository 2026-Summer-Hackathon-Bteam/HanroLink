package com.hanrolink.region.response.component;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegionOptionResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short sortOrder
) {}
