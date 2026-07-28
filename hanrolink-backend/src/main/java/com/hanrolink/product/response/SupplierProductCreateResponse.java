package com.hanrolink.product.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductCreateResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long productId
) {}
