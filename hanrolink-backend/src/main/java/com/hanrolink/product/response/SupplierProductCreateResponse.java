package com.hanrolink.product.response;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductCreateResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long productId
) {
  public SupplierProductCreateResponse {
    Objects.requireNonNull(
      productId,
      "SupplierProductCreateResponse.productId must not be null"
    );
  }
}
