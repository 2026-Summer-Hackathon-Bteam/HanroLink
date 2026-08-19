package com.hanrolink.negotiationrequest.response.component;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductNegotiationRequestBuyerResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID businessId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName
) {
  public SupplierProductNegotiationRequestBuyerResponse {
    Objects.requireNonNull(
      businessId,
      "SupplierProductNegotiationRequestBuyerResponse.businessId must not be null"
    );

    Objects.requireNonNull(
      businessName,
      "SupplierProductNegotiationRequestBuyerResponse.businessName must not be null"
    );
  }
}
