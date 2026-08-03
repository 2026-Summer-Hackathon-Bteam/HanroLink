package com.hanrolink.businessapproval.response.component;

import java.time.Instant;
import java.util.UUID;

import com.hanrolink.account.enums.BusinessUserAccountReviewStatus;
import com.hanrolink.account.enums.BusinessUserAccountRole;

import io.swagger.v3.oas.annotations.media.Schema;

public record BusinessApprovalAccountResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BusinessUserAccountRole role,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BusinessUserAccountReviewStatus reviewStatus,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String lastName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String firstName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String lastNameKana,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String firstNameKana,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String phoneNumber,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String email,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant createdAt
) {}
