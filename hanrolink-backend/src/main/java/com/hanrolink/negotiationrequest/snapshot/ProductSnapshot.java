package com.hanrolink.negotiationrequest.snapshot;

import java.time.Instant;
import java.util.List;

import com.hanrolink.negotiationrequest.snapshot.component.MainIngredientOriginPrefectureSnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.MonthlySupplyCapacitySnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.ProductCategorySnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.ProductExpirationTypeSnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.ProductStorySnapshot;
import com.hanrolink.negotiationrequest.snapshot.component.StorageTypeSnapshot;

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
) {}
