package com.hanrolink.product.response.component;

import java.util.Objects;

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
) {
  public ProductStorySectionTemplateOptionResponse {
    Objects.requireNonNull(
      id,
      "ProductStorySectionTemplateOptionResponse.id must not be null"
    );

    Objects.requireNonNull(
      title,
      "ProductStorySectionTemplateOptionResponse.title must not be null"
    );

    Objects.requireNonNull(
      imageHint,
      "ProductStorySectionTemplateOptionResponse.imageHint must not be null"
    );

    Objects.requireNonNull(
      bodyHelpText,
      "ProductStorySectionTemplateOptionResponse.bodyHelpText must not be null"
    );

    Objects.requireNonNull(
      bodyExample,
      "ProductStorySectionTemplateOptionResponse.bodyExample must not be null"
    );

    Objects.requireNonNull(
      sortOrder,
      "ProductStorySectionTemplateOptionResponse.sortOrder must not be null"
    );
  }
}
