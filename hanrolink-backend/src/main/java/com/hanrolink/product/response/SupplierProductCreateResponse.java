package com.hanrolink.product.response;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductCreateResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id
) {
  public SupplierProductCreateResponse {
    Objects.requireNonNull(
      id,
      "SupplierProductCreateResponse.id must not be null"
    );
  }
}
