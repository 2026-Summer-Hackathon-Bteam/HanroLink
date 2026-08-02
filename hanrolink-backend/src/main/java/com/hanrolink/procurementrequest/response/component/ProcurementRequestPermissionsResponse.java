package com.hanrolink.procurementrequest.response.component;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestPermissionsResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean canManage,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean canCreateNegotiationRequest
) {}
