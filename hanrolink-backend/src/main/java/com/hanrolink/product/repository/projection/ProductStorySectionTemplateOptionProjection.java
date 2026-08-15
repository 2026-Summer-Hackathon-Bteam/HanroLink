package com.hanrolink.product.repository.projection;

public record ProductStorySectionTemplateOptionProjection(
  Short id,
  String title,
  String imageHint,
  String bodyHelpText,
  String bodyExample
) {}
