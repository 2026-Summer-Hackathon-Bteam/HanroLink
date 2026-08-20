package com.hanrolink.product.repository.projection;

public record ProductStorySnapshotProjection(
  Short productStorySectionTemplateId,
  String sectionTitle,
  String body
) {}
