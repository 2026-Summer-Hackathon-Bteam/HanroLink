package com.hanrolink.product.response;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record PublicProductListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String supplierBusinessName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String mainImageUrl
) {
  public PublicProductListResponse {
    Objects.requireNonNull(
      name,
      "PublicProductListResponse.name must not be null"
    );

    Objects.requireNonNull(
      supplierBusinessName,
      "PublicProductListResponse.supplierBusinessName must not be null"
    );

    Objects.requireNonNull(
      mainImageUrl,
      "PublicProductListResponse.mainImageUrl must not be null"
    );
  }
}
