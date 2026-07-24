package com.hanrolink.procurementrequest.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementRequestCreateResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long procurementRequestId
) {}
