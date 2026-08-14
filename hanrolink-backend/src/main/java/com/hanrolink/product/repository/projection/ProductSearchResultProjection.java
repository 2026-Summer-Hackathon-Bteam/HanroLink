package com.hanrolink.product.repository.projection;

import java.util.UUID;

public record ProductSearchResultProjection(
  Long id,
  UUID publicId,
  String name,
  String businessName,
  String productCategoryName,
  String mainIngredientRegionName,
  String mainImageStorageKey
) {}
