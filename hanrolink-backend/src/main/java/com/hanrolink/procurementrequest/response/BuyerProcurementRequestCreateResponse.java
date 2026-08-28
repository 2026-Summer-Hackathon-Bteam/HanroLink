package com.hanrolink.procurementrequest.response;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementRequestCreateResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id
) {
  public BuyerProcurementRequestCreateResponse {
    Objects.requireNonNull(
      id,
      "BuyerProcurementRequestCreateResponse.id must not be null"
    );
  }
}
