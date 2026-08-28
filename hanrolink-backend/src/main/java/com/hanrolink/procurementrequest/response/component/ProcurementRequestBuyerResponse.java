package com.hanrolink.procurementrequest.response.component;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestBuyerResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID businessId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName
) {
  public ProcurementRequestBuyerResponse {
    Objects.requireNonNull(
      businessId,
      "ProcurementRequestBuyerResponse.businessId must not be null"
    );

    Objects.requireNonNull(
      businessName,
      "ProcurementRequestBuyerResponse.businessName must not be null"
    );
  }
}
