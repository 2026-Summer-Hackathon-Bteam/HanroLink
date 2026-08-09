package com.hanrolink.businessapproval.response;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdminBusinessApprovalListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID businessUserAccountId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant createdAt
) {
  public AdminBusinessApprovalListResponse {
    Objects.requireNonNull(
      businessUserAccountId,
      "AdminBusinessApprovalListResponse.businessUserAccountId must not be null"
    );

    Objects.requireNonNull(
      businessName,
      "AdminBusinessApprovalListResponse.businessName must not be null"
    );

    Objects.requireNonNull(
      createdAt,
      "AdminBusinessApprovalListResponse.createdAt must not be null"
    );
  }
}
