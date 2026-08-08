package com.hanrolink.negotiationrequest.response.component;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductNegotiationRequestBuyerResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID accountId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName
) {
  public SupplierProductNegotiationRequestBuyerResponse {
    Objects.requireNonNull(
      accountId,
      "SupplierProductNegotiationRequestBuyerResponse.accountId must not be null"
    );

    Objects.requireNonNull(
      businessName,
      "SupplierProductNegotiationRequestBuyerResponse.businessName must not be null"
    );
  }
}
