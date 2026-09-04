package com.hanrolink.product.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductPermissionsResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean canManage,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean canCreateNegotiationRequest
) {
  public ProductPermissionsResponse {
    Objects.requireNonNull(
      canManage,
      "ProductPermissionsResponse.canManage must not be null"
    );

    Objects.requireNonNull(
      canCreateNegotiationRequest,
      "ProductPermissionsResponse.canCreateNegotiationRequest must not be null"
    );
  }
}
