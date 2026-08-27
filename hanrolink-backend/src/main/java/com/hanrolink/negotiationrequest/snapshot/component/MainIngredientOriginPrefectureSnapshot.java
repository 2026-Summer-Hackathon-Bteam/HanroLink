package com.hanrolink.negotiationrequest.snapshot.component;

import java.util.Objects;

public record MainIngredientOriginPrefectureSnapshot(
  Short id,
  String name
) {
  public MainIngredientOriginPrefectureSnapshot {
    Objects.requireNonNull(
      id,
      "MainIngredientOriginPrefectureSnapshot.id must not be null"
    );

    Objects.requireNonNull(
      name,
      "MainIngredientOriginPrefectureSnapshot.name must not be null"
    );
  }
}
