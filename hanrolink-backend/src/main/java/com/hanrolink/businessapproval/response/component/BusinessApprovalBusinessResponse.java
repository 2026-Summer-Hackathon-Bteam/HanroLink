package com.hanrolink.businessapproval.response.component;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.hanrolink.business.enums.BusinessReviewStatus;
import com.hanrolink.business.enums.BusinessRole;

import io.swagger.v3.oas.annotations.media.Schema;

public record BusinessApprovalBusinessResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BusinessRole role,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  BusinessReviewStatus reviewStatus,

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
  String phoneNumber,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant createdAt
) {
  public BusinessApprovalBusinessResponse {
    Objects.requireNonNull(
      id,
      "BusinessApprovalBusinessResponse.id must not be null"
    );

    Objects.requireNonNull(
      role,
      "BusinessApprovalBusinessResponse.role must not be null"
    );

    Objects.requireNonNull(
      reviewStatus,
      "BusinessApprovalBusinessResponse.reviewStatus must not be null"
    );

    Objects.requireNonNull(
      name,
      "BusinessApprovalBusinessResponse.name must not be null"
    );

    Objects.requireNonNull(
      nameKana,
      "BusinessApprovalBusinessResponse.nameKana must not be null"
    );

    Objects.requireNonNull(
      addressPostalCode,
      "BusinessApprovalBusinessResponse.addressPostalCode must not be null"
    );

    Objects.requireNonNull(
      addressPrefecture,
      "BusinessApprovalBusinessResponse.addressPrefecture must not be null"
    );

    Objects.requireNonNull(
      addressMunicipalityStreet,
      "BusinessApprovalBusinessResponse.addressMunicipalityStreet must not be null"
    );

    Objects.requireNonNull(
      phoneNumber,
      "BusinessApprovalBusinessResponse.phoneNumber must not be null"
    );

    Objects.requireNonNull(
      createdAt,
      "BusinessApprovalBusinessResponse.createdAt must not be null"
    );
  }
}