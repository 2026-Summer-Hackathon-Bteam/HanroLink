package com.hanrolink.businessapproval.response;

import java.time.Instant;
import java.util.UUID;

import com.hanrolink.account.enums.BusinessUserAccountReviewStatus;
import com.hanrolink.account.enums.BusinessUserAccountRole;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdminBusinessApprovalDetailResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BusinessUserAccount businessUserAccount,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Business business
) {

  public record BusinessUserAccount(
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

  public record Business(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String nameKana,

    String websiteUrl,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String addressPostalCode,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String addressPrefecture,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String addressMunicipalityStreet,

    String addressBuilding,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String phoneNumber
  ) {}
}