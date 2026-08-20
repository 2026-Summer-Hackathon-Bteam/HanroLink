package com.hanrolink.negotiationrequest.snapshot;

import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanrolink.product.enums.ProductExpirationType;
import com.hanrolink.product.enums.StorageType;

public record ProductSnapshot(
  Instant sourceUpdatedAt,
  ProductCategorySnapshot productCategory,
  MainIngredientOriginPrefectureSnapshot mainIngredientOriginPrefecture,
  String name,
  String contentQuantity,
  ProductExpirationTypeSnapshot expirationType,
  Short shelfLifeDays,
  StorageTypeSnapshot storageType,
  Integer desiredRetailPrice,
  String allergyInformation,
  String certificationInformation,
  String caseSize,
  Integer unitsPerCase,
  Integer minimumOrderQuantity,
  Short shippingLeadTimeDays,
  String salesAreaRestriction,
  List<MonthlySupplyCapacitySnapshot> monthlySupplyCapacities,
  List<ProductStorySnapshot> productStories
) {
  public record ProductCategorySnapshot(
    Short id,
    String name
  ) {}

  public record MainIngredientOriginPrefectureSnapshot(
    Short id,
    String name
  ) {}

  public record ProductExpirationTypeSnapshot(
    ProductExpirationType value,
    String displayName
  ) {}

  public record StorageTypeSnapshot(
    StorageType value,
    String displayName
  ) {}

  public record MonthlySupplyCapacitySnapshot(
    @JsonFormat(pattern = "yyyy-MM")
    YearMonth targetMonth,
    Integer availableQuantity
  ) {}

  public record ProductStorySnapshot(
    String sectionTitle,
    String body
  ) {}
}
