package com.hanrolink.product.repository.projection;

public record PublicProductListProjection(
  String name,

  String supplierBusinessName,

  String mainImageStorageKey
) {}
