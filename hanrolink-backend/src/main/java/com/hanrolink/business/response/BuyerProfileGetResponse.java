package com.hanrolink.business.response;

import java.util.Objects;

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
) {
  public BuyerProfileGetResponse {
    Objects.requireNonNull(
      businessName,
      "BuyerProfileGetResponse.businessName must not be null"
    );

    Objects.requireNonNull(
      businessAddressPrefecture,
      "BuyerProfileGetResponse.businessAddressPrefecture must not be null"
    );

    Objects.requireNonNull(
      businessAddressMunicipalityStreet,
      "BuyerProfileGetResponse.businessAddressMunicipalityStreet must not be null"
    );
  }
}
