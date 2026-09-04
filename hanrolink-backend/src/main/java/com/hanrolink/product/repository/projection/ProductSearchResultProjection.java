package com.hanrolink.product.repository.projection;

import java.util.UUID;

import com.hanrolink.product.enums.StorageType;

public record ProductSearchResultProjection(
  Long id,
  UUID publicId,
  String name,
  String businessName,
  String productCategoryName,
  String mainIngredientOriginPrefectureName,
  StorageType storageType,
  String mainImageStorageKey
) {}
