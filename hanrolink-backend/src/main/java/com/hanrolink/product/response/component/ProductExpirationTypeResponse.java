package com.hanrolink.product.response.component;

import java.util.Objects;

import com.hanrolink.product.enums.ProductExpirationType;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductExpirationTypeResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductExpirationType value,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String label
) {
  public ProductExpirationTypeResponse {
    Objects.requireNonNull(
      value,
      "ProductExpirationTypeResponse.value must not be null"
    );

    Objects.requireNonNull(
      label,
      "ProductExpirationTypeResponse.label must not be null"
    );
  }
}
