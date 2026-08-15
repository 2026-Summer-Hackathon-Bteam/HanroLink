package com.hanrolink.product.repository.projection;

import java.time.Instant;
import java.util.UUID;

public record SupplierProductListProjection(
  UUID publicId,
  String name,
  String mainImageStorageKey,
  Instant hiddenAt,
  Instant updatedAt
) {}
