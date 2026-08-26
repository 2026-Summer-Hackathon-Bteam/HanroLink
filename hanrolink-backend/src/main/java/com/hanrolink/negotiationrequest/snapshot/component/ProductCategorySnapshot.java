package com.hanrolink.negotiationrequest.snapshot.component;

import java.util.Objects;

public record ProductCategorySnapshot(
  Short id,
  String name
) {
  public ProductCategorySnapshot {
    Objects.requireNonNull(
      id,
      "ProductCategorySnapshot.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "ProductCategorySnapshot.name must not be null"
    );
  }
}
