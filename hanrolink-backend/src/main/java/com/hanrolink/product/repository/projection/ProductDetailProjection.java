package com.hanrolink.product.repository.projection;

import java.time.Instant;

import com.hanrolink.product.enums.ProductExpirationType;
import com.hanrolink.product.enums.StorageType;

public record ProductDetailProjection(
  Long id,
  Long supplierAccountId,
  String name,
  Instant hiddenAt,
  Short productCategoryId,
  String productCategoryName,
  Short mainIngredientRegionId,
  String mainIngredientRegionName,
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
