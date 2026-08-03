package com.hanrolink.businessapproval.response.component;

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
) {}