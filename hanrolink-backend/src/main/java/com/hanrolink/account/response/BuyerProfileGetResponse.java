package com.hanrolink.account.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProfileGetResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessAddressPrefecture,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessAddressMunicipalityStreet,

  String businessAddressBuilding,

  String websiteUrl
) {}
