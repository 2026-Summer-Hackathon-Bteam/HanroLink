package com.hanrolink.negotiationrequest.response;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierNegotiationRequestSelectableProductResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String mainImageUrl
) {
  public SupplierNegotiationRequestSelectableProductResponse {
    Objects.requireNonNull(
      id,
      "SupplierNegotiationRequestSelectableProductResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "SupplierNegotiationRequestSelectableProductResponse.name must not be null"
    );

    Objects.requireNonNull(
      mainImageUrl,
      "SupplierNegotiationRequestSelectableProductResponse.mainImageUrl must not be null"
    );
  }
}
