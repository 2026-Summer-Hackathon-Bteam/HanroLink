package com.hanrolink.product.response.component;

import com.hanrolink.product.enums.ProductExpirationType;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductExpirationTypeOptionResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductExpirationType value,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String label
) {}
