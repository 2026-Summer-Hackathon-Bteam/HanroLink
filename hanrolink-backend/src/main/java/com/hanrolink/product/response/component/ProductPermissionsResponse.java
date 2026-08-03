package com.hanrolink.product.response.component;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductPermissionsResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean canManage,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean canCreateNegotiationRequest
) {}
