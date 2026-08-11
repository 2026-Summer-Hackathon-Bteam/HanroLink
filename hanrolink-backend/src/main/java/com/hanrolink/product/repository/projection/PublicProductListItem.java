package com.hanrolink.product.repository.projection;

public record PublicProductListItem(
  String name,

  String supplierBusinessName,

  String mainImageStorageKey
) {}
