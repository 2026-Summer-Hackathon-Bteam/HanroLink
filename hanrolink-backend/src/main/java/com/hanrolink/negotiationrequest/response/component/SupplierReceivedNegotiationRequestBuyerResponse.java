package com.hanrolink.negotiationrequest.response.component;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierReceivedNegotiationRequestBuyerResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID businessId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName
) {
  public SupplierReceivedNegotiationRequestBuyerResponse {
    Objects.requireNonNull(
      businessId,
      "SupplierReceivedNegotiationRequestBuyerResponse.businessId must not be null"
    );

    Objects.requireNonNull(
      businessName,
      "SupplierReceivedNegotiationRequestBuyerResponse.businessName must not be null"
    );
  }
}
