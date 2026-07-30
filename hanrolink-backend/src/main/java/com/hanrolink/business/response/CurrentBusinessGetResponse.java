package com.hanrolink.business.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CurrentBusinessGetResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String businessName
) {}
