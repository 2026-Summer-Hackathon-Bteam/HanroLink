package com.hanrolink.product.repository.projection;

import java.time.Instant;

public record SupplierProductManagementListItem(
  Long id,

  String name,

  String mainImageStorageKey,

  Instant hiddenAt,

  Instant updatedAt
) {}
