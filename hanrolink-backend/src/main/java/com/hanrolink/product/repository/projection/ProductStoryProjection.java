package com.hanrolink.product.repository.projection;

public record ProductStoryProjection(
  Long id,

  Short productStorySectionTemplateId,

  Short position,

  String sectionTitle,

  String body,

  String imageStorageKey
) {}
