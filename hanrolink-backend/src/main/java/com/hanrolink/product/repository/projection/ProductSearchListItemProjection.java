package com.hanrolink.product.repository.projection;

public record ProductSearchListItemProjection(
  Long id,
  String name,
  String businessName,
  String productCategoryName,
  String mainIngredientRegionName,
  String mainImageStorageKey
) {}
