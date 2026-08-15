package com.hanrolink.productcategory.repository.projection;

public record ProductCategoryOptionProjection(
  Short id,
  Short productCategoryGroupId,
  String name
) {}
