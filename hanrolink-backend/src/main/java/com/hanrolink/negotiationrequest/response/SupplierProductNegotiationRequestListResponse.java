package com.hanrolink.negotiationrequest.response;

import java.time.Instant;

import com.hanrolink.negotiationrequest.response.component.ProductSnapshotSummaryResponse;
import com.hanrolink.negotiationrequest.response.component.SupplierProductNegotiationRequestBuyerResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductNegotiationRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long productNegotiationRequestId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductSnapshotSummaryResponse product,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  SupplierProductNegotiationRequestBuyerResponse buyer,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant expiresAt
) {}
