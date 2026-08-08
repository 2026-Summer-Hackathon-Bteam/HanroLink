package com.hanrolink.negotiationrequest.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestSnapshotSummaryResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String title
) {
  public ProcurementRequestSnapshotSummaryResponse {
    Objects.requireNonNull(
      id,
      "ProcurementRequestSnapshotSummaryResponse.id must not be null"
    );

    Objects.requireNonNull(
      title,
      "ProcurementRequestSnapshotSummaryResponse.title must not be null"
    );
  }
}