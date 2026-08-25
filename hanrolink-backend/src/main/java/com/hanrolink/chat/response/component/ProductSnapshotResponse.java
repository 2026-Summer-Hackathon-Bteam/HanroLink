package com.hanrolink.chat.response.component;

import java.util.List;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductSnapshotResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String productCategoryName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String mainIngredientOriginPrefectureName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String contentQuantity,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String expirationTypeName,

  Short shelfLifeDays,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String storageTypeName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Integer desiredRetailPrice,

  String allergyInformation,

  String certificationInformation,

  String caseSize,

  Integer unitsPerCase,

  Integer minimumOrderQuantity,

  Short shippingLeadTimeDays,

  String salesAreaRestriction,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<MonthlySupplyCapacitySnapshotResponse> monthlySupplyCapacities,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductStorySnapshotResponse> productStories
) {
  public ProductSnapshotResponse {
    Objects.requireNonNull(
      productCategoryName,
      "ProductSnapshotResponse.productCategoryName must not be null"
    );

    Objects.requireNonNull(
      mainIngredientOriginPrefectureName,
      "ProductSnapshotResponse.mainIngredientOriginPrefectureName must not be null"
    );

    Objects.requireNonNull(
      name,
      "ProductSnapshotResponse.name must not be null"
    );

    Objects.requireNonNull(
      contentQuantity,
      "ProductSnapshotResponse.contentQuantity must not be null"
    );

    Objects.requireNonNull(
      expirationTypeName,
      "ProductSnapshotResponse.expirationTypeName must not be null"
    );

    Objects.requireNonNull(
      storageTypeName,
      "ProductSnapshotResponse.storageTypeName must not be null"
    );

    Objects.requireNonNull(
      desiredRetailPrice,
      "ProductSnapshotResponse.desiredRetailPrice must not be null"
    );

    Objects.requireNonNull(
      monthlySupplyCapacities,
      "ProductSnapshotResponse.monthlySupplyCapacities must not be null"
    );

    Objects.requireNonNull(
      productStories,
      "ProductSnapshotResponse.productStories must not be null"
    );
  }
}
