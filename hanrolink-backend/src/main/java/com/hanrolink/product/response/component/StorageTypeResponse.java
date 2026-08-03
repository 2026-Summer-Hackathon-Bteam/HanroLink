package com.hanrolink.product.response.component;

import com.hanrolink.product.enums.StorageType;

import io.swagger.v3.oas.annotations.media.Schema;

public record StorageTypeResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  StorageType value,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String label
) {}
