package com.hanrolink.negotiationrequest.response;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.hanrolink.negotiationrequest.response.component.NegotiationRequestProductResponse;
import com.hanrolink.negotiationrequest.response.component.NegotiationRequestSenderBusinessResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierReceivedNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  NegotiationRequestProductResponse product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  NegotiationRequestSenderBusinessResponse senderBusiness,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {
  public SupplierReceivedNegotiationRequestListResponse {
    Objects.requireNonNull(
      id,
      "SupplierReceivedNegotiationRequestListResponse.id must not be null"
    );

    Objects.requireNonNull(
      product,
      "SupplierReceivedNegotiationRequestListResponse.product must not be null"
    );

    Objects.requireNonNull(
      senderBusiness,
      "SupplierReceivedNegotiationRequestListResponse.senderBusiness must not be null"
    );

    Objects.requireNonNull(
      expiresAt,
      "SupplierReceivedNegotiationRequestListResponse.expiresAt must not be null"
    );
  }
}
