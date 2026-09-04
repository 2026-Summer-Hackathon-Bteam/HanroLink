package com.hanrolink.product.repository.projection;

import java.util.UUID;

public record PublicProductListProjection(
  UUID publicId,
  String name,
  String supplierBusinessName,
  String mainImageStorageKey
) {}
