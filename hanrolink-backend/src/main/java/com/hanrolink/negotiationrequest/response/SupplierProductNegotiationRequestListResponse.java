package com.hanrolink.negotiationrequest.response;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProductResponse;
import com.hanrolink.negotiationrequest.response.component.SupplierProductNegotiationRequestBuyerResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  NegotiationRequestProductResponse product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  SupplierProductNegotiationRequestBuyerResponse buyer,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {
  public SupplierProductNegotiationRequestListResponse {
    Objects.requireNonNull(
      id,
      "SupplierProductNegotiationRequestListResponse.id must not be null"
    );

    Objects.requireNonNull(
      product,
      "SupplierProductNegotiationRequestListResponse.product must not be null"
    );

    Objects.requireNonNull(
      buyer,
      "SupplierProductNegotiationRequestListResponse.buyer must not be null"
    );

    Objects.requireNonNull(
      expiresAt,
      "SupplierProductNegotiationRequestListResponse.expiresAt must not be null"
    );
  }
}
