package com.hanrolink.negotiationrequest.snapshot;

import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ProductSnapshot(
  Instant sourceUpdatedAt,
  String productCategoryName,
  String mainIngredientRegionName,
  String name,
  String contentQuantity,
  String productExpirationName,
  Short shelfLifeDays,
  String storageTypeName,
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
  public record MonthlySupplyCapacitySnapshot(
    @JsonFormat(pattern = "yyyy-MM")
    YearMonth targetMonth,
    Integer availableQuantity
  ) {}

  public record ProductStorySnapshot(
    Short position,
    String sectionTitle,
    String body,
    String imageStorageKey
  ) {}
}
