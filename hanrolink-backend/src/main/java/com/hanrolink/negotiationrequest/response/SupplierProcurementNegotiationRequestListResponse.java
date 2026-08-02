package com.hanrolink.negotiationrequest.response;

import java.time.Instant;

import com.hanrolink.negotiationrequest.response.component.ProcurementRequestSnapshotSummaryResponse;
import com.hanrolink.negotiationrequest.response.component.ProductSnapshotSummaryResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProcurementNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long procurementNegotiationRequestId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProcurementRequestSnapshotSummaryResponse procurementRequest,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductSnapshotSummaryResponse product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {}
