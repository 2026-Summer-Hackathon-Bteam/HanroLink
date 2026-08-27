package com.hanrolink.negotiationrequest.snapshot;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

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
) {
  public ProductSnapshot {
    Objects.requireNonNull(
      sourceUpdatedAt,
      "ProductSnapshot.sourceUpdatedAt must not be null"
    );

    Objects.requireNonNull(
      productCategory,
      "ProductSnapshot.productCategory must not be null"
    );

    Objects.requireNonNull(
      mainIngredientOriginPrefecture,
      "ProductSnapshot.mainIngredientOriginPrefecture must not be null"
    );

    Objects.requireNonNull(
      name,
      "ProductSnapshot.name must not be null"
    );

    Objects.requireNonNull(
      contentQuantity,
      "ProductSnapshot.contentQuantity must not be null"
    );

    Objects.requireNonNull(
      expirationType,
      "ProductSnapshot.expirationType must not be null"
    );

    Objects.requireNonNull(
      storageType,
      "ProductSnapshot.storageType must not be null"
    );

    Objects.requireNonNull(
      desiredRetailPrice,
      "ProductSnapshot.desiredRetailPrice must not be null"
    );

    if (monthlySupplyCapacities == null
      || monthlySupplyCapacities.isEmpty()
      || monthlySupplyCapacities.stream().anyMatch(Objects::isNull)
    ) {
      throw new IllegalArgumentException(
        "ProductSnapshot.monthlySupplyCapacities must be a non-empty list without null elements"
      );
    }
    monthlySupplyCapacities = List.copyOf(monthlySupplyCapacities);

    if (productStories == null
      || productStories.isEmpty()
      || productStories.stream().anyMatch(Objects::isNull)
    ) {
      throw new IllegalArgumentException(
        "ProductSnapshot.productStories must be a non-empty list without null elements"
      );
    }
    productStories = List.copyOf(productStories);
  }
}
