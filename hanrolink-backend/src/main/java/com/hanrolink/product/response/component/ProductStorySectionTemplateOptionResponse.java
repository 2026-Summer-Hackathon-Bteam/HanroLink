package com.hanrolink.product.response.component;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductStorySectionTemplateOptionResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String title,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String imageHint,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String bodyHelpText,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String bodyExample,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short sortOrder
) {}
