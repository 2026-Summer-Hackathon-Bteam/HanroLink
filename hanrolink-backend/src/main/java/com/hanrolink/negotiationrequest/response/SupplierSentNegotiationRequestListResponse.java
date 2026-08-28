package com.hanrolink.negotiationrequest.response;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProcurementRequestResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProductResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierSentNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  NegotiationRequestProcurementRequestResponse procurementRequest,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  NegotiationRequestProductResponse product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {
  public SupplierSentNegotiationRequestListResponse {
    Objects.requireNonNull(
      id,
      "SupplierSentNegotiationRequestListResponse.id must not be null"
    );

    Objects.requireNonNull(
      procurementRequest,
      "SupplierSentNegotiationRequestListResponse.procurementRequest must not be null"
    );

    Objects.requireNonNull(
      product,
      "SupplierSentNegotiationRequestListResponse.product must not be null"
    );

    Objects.requireNonNull(
      expiresAt,
      "SupplierSentNegotiationRequestListResponse.expiresAt must not be null"
    );
  }
}
