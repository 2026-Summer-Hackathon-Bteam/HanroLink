package com.hanrolink.product.repository.projection;

import java.util.UUID;

public record SupplierNegotiationRequestSelectableProductProjection(
  UUID publicId,
  String name,
  String mainImageStorageKey
) {}
