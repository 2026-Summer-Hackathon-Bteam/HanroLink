package com.hanrolink.procurementrequest.response;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementRequestCreateResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long procurementRequestId
) {
  public BuyerProcurementRequestCreateResponse {
    Objects.requireNonNull(
      procurementRequestId,
      "BuyerProcurementRequestCreateResponse.procurementRequestId must not be null"
    );
  }
}
