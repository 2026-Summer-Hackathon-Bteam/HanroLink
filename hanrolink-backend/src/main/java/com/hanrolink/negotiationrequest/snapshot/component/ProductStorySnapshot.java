package com.hanrolink.negotiationrequest.snapshot.component;

import java.util.Objects;

public record ProductStorySnapshot(
  Short productStorySectionTemplateId,
  String sectionTitle,
  String body
) {
  public ProductStorySnapshot {
    Objects.requireNonNull(
      productStorySectionTemplateId,
      "ProductStorySnapshot.productStorySectionTemplateId must not be null"
    );

    Objects.requireNonNull(
      sectionTitle,
      "ProductStorySnapshot.sectionTitle must not be null"
    );

    Objects.requireNonNull(
      body,
      "ProductStorySnapshot.body must not be null"
    );
  }
}
