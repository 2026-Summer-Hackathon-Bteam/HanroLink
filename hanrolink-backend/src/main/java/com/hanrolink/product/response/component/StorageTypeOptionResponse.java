package com.hanrolink.product.response.component;

import java.util.Objects;

import com.hanrolink.product.enums.StorageType;

import io.swagger.v3.oas.annotations.media.Schema;

public record StorageTypeOptionResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  StorageType value,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String label
) {
  public StorageTypeOptionResponse {
    Objects.requireNonNull(
      value,
      "StorageTypeOptionResponse.value must not be null"
    );

    Objects.requireNonNull(
      label,
      "StorageTypeOptionResponse.label must not be null"
    );
  }
}
