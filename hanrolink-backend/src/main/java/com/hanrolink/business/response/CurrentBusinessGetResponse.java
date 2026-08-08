package com.hanrolink.business.response;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrentBusinessGetResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName
) {
  public CurrentBusinessGetResponse {
    Objects.requireNonNull(
      businessName,
      "CurrentBusinessGetResponse.businessName must not be null"
    );
  }
}
