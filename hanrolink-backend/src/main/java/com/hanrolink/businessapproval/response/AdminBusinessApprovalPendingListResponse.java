package com.hanrolink.businessapproval.response;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdminBusinessApprovalPendingListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID businessUserAccountId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant createdAt
) {}
