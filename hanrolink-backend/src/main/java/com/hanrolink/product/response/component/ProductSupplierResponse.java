package com.hanrolink.product.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductSupplierResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessAddressPrefecture,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessAddressMunicipalityStreet,

  String businessAddressBuilding,

  String businessWebsiteUrl
) {
  public ProductSupplierResponse {
    Objects.requireNonNull(
      businessName,
      "ProductSupplierResponse.businessName must not be null"
    );

    Objects.requireNonNull(
      businessAddressPrefecture,
      "ProductSupplierResponse.businessAddressPrefecture must not be null"
    );

    Objects.requireNonNull(
      businessAddressMunicipalityStreet,
      "ProductSupplierResponse.businessAddressMunicipalityStreet must not be null"
    );
  }
}
