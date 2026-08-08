package com.hanrolink.procurementrequest.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestPermissionsResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean canManage,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean canCreateNegotiationRequest
) {
  public ProcurementRequestPermissionsResponse {
    Objects.requireNonNull(
      canManage,
      "ProcurementRequestPermissionsResponse.canManage must not be null"
    );

    Objects.requireNonNull(
      canCreateNegotiationRequest,
      "ProcurementRequestPermissionsResponse.canCreateNegotiationRequest must not be null"
    );
  }
}
