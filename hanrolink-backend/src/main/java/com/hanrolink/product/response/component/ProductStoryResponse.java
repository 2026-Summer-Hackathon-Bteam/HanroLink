package com.hanrolink.product.response.component;

import java.util.Objects;

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
) {
  public ProductStoryResponse {
    Objects.requireNonNull(
      id,
      "ProductStoryResponse.id must not be null"
    );

    Objects.requireNonNull(
      productStorySectionTemplateId,
      "ProductStoryResponse.productStorySectionTemplateId must not be null"
    );

    Objects.requireNonNull(
      position,
      "ProductStoryResponse.position must not be null"
    );

    Objects.requireNonNull(
      sectionTitle,
      "ProductStoryResponse.sectionTitle must not be null"
    );

    Objects.requireNonNull(
      body,
      "ProductStoryResponse.body must not be null"
    );

    Objects.requireNonNull(
      imageUrl,
      "ProductStoryResponse.imageUrl must not be null"
    );
  }
}
