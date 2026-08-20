package com.hanrolink.product.repository.projection;

import java.time.Instant;

import com.hanrolink.product.enums.ProductExpirationType;
import com.hanrolink.product.enums.StorageType;

public record ProductSnapshotProjection(
  Instant updatedAt,
  Long id,
  Short productCategoryId,
  String productCategoryName,
  Short mainIngredientOriginPrefectureId,
  String mainIngredientOriginPrefectureName,
  String name,
  String contentQuantity,
  ProductExpirationType expirationType,
  Short shelfLifeDays,
  StorageType storageType,
  Integer desiredRetailPrice,
  String allergyInformation,
  String certificationInformation,
  String caseSize,
  Integer unitsPerCase,
  Integer minimumOrderQuantity,
  Short shippingLeadTimeDays,
  String salesAreaRestriction
) {}
