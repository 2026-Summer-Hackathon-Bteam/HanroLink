package com.hanrolink.negotiationrequest.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductSnapshotSummaryResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name
) {
  public ProductSnapshotSummaryResponse {
    Objects.requireNonNull(
      id,
      "ProductSnapshotSummaryResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "ProductSnapshotSummaryResponse.name must not be null"
    );
  }
}
