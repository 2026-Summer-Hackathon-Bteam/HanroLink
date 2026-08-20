package com.hanrolink.product.repository.projection;

import java.time.Instant;
import java.util.UUID;

import com.hanrolink.product.enums.ProductExpirationType;
import com.hanrolink.product.enums.StorageType;

public record ProductDetailProjection(
  Long id,
  UUID publicId,
  Long supplierBusinessId,
  String name,
  Instant hiddenAt,
  Short productCategoryId,
  String productCategoryName,
  Short mainIngredientOriginPrefectureId,
  String mainIngredientOriginPrefectureName,
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
  String salesAreaRestriction,
  String mainImageStorageKey,
  String supplierBusinessName,
  String supplierBusinessAddressPrefecture,
  String supplierBusinessAddressMunicipalityStreet,
  String supplierBusinessAddressBuilding,
  String supplierBusinessWebsiteUrl
) {}
