package com.hanrolink.product.response;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String mainImageUrl,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean hidden,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant updatedAt
) {
  public SupplierProductListResponse {
    Objects.requireNonNull(
      id,
      "SupplierProductListResponse.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "SupplierProductListResponse.name must not be null"
    );

    Objects.requireNonNull(
      mainImageUrl,
      "SupplierProductListResponse.mainImageUrl must not be null"
    );

    Objects.requireNonNull(
      hidden,
      "SupplierProductListResponse.hidden must not be null"
    );

    Objects.requireNonNull(
      updatedAt,
      "SupplierProductListResponse.updatedAt must not be null"
    );
  }
}
