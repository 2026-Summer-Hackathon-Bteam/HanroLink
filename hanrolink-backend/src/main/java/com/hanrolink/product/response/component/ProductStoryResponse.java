package com.hanrolink.product.response.component;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductStoryResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short productStorySectionTemplateId,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Short position,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String sectionTitle,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String body,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String imageUrl
) {}
