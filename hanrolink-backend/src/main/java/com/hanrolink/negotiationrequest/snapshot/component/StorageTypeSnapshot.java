package com.hanrolink.negotiationrequest.snapshot.component;

import java.util.Objects;

import com.hanrolink.product.enums.StorageType;

public record StorageTypeSnapshot(
  StorageType value,
  String displayName
) {
  public StorageTypeSnapshot {
    Objects.requireNonNull(
      value,
      "StorageTypeSnapshot.value must not be null"
    );

    Objects.requireNonNull(
      displayName,
      "StorageTypeSnapshot.displayName must not be null"
    );
  }
}
