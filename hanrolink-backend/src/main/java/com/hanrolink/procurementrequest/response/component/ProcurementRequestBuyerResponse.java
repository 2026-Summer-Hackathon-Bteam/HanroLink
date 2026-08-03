package com.hanrolink.procurementrequest.response.component;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestBuyerResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID accountId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName
) {}
