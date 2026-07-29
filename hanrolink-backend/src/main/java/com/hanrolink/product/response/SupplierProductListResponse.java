package com.hanrolink.product.response;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String mainImageUrl,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean hidden,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant updatedAt
) {}
