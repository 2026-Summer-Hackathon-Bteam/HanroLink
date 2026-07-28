package com.hanrolink.product.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record PublicProductListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String supplierName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String mainImageUrl
) {}
