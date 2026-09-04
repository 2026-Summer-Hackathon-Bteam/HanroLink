package com.hanrolink.product.response.component;

import java.util.Objects;

import com.hanrolink.product.enums.ProductExpirationType;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductExpirationTypeOptionResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductExpirationType value,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String label
) {
  public ProductExpirationTypeOptionResponse {
    Objects.requireNonNull(
      value,
      "ProductExpirationTypeOptionResponse.value must not be null"
    );

    Objects.requireNonNull(
      label,
      "ProductExpirationTypeOptionResponse.label must not be null"
    );
  }
}
