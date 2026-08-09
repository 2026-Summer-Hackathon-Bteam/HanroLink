package com.hanrolink.businessapproval.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record BusinessApprovalBusinessResponse(
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
) {
  public BusinessApprovalBusinessResponse {
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
  }
}