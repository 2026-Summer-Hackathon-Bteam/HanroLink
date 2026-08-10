package com.hanrolink.region.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegionOptionResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name
) {
  public RegionOptionResponse {
    Objects.requireNonNull(
      id,
      "RegionOptionResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "RegionOptionResponse.name must not be null"
    );
  }
}
