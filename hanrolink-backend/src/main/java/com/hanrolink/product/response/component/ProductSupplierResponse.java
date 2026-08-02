package com.hanrolink.product.response.component;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductSupplierResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessAddressPrefecture,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessAddressMunicipalityStreet,

  String businessAddressBuilding,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessWebsiteUrl
) {}
