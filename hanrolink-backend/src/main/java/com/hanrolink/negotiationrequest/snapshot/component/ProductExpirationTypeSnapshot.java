package com.hanrolink.negotiationrequest.snapshot.component;

import com.hanrolink.product.enums.ProductExpirationType;

public record ProductExpirationTypeSnapshot(
  ProductExpirationType value,
  String displayName
) {}
