package com.hanrolink.chat.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductStorySnapshotResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String sectionTitle,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String body
) {
  public ProductStorySnapshotResponse {
    Objects.requireNonNull(
      sectionTitle,
      "ProductStorySnapshotResponse.sectionTitle must not be null"
    );

    Objects.requireNonNull(
      body,
      "ProductStorySnapshotResponse.body must not be null"
    );
  }
}
