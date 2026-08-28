package com.hanrolink.product.response;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductCreateResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id
) {
  public SupplierProductCreateResponse {
    Objects.requireNonNull(
      id,
      "SupplierProductCreateResponse.id must not be null"
    );
  }
}
