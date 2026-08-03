package com.hanrolink.negotiationrequest.response;

import java.time.Instant;

import com.hanrolink.negotiationrequest.response.component.ProcurementRequestSnapshotSummaryResponse;
import com.hanrolink.negotiationrequest.response.component.BuyerProcurementNegotiationProductSnapshotSummaryResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long procurementNegotiationRequestId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProcurementRequestSnapshotSummaryResponse procurementRequest,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BuyerProcurementNegotiationProductSnapshotSummaryResponse product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {}
