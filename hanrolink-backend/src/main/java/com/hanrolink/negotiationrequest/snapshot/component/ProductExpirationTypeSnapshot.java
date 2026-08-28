package com.hanrolink.negotiationrequest.snapshot.component;

import java.util.Objects;

import com.hanrolink.product.enums.ProductExpirationType;

public record ProductExpirationTypeSnapshot(
  ProductExpirationType value,
  String displayName
) {
  public ProductExpirationTypeSnapshot {
    Objects.requireNonNull(
      value,
      "ProductExpirationTypeSnapshot.value must not be null"
    );

    Objects.requireNonNull(
      displayName,
      "ProductExpirationTypeSnapshot.displayName must not be null"
    );
  }
}
