package com.hanrolink.region.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record PrefectureOptionResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name
) {
  public PrefectureOptionResponse {
    Objects.requireNonNull(
      id,
      "PrefectureOptionResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "PrefectureOptionResponse.name must not be null"
    );
  }
}
