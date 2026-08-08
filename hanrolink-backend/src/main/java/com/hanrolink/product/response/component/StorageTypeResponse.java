package com.hanrolink.product.response.component;

import java.util.Objects;

import com.hanrolink.product.enums.StorageType;

import io.swagger.v3.oas.annotations.media.Schema;

public record StorageTypeResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  StorageType value,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String label
) {
  public StorageTypeResponse {
    Objects.requireNonNull(
      value,
      "StorageTypeResponse.value must not be null"
    );

    Objects.requireNonNull(
      label,
      "StorageTypeResponse.label must not be null"
    );
  }
}
