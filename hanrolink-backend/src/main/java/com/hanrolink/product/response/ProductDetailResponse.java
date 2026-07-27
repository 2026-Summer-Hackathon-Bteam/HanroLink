package com.hanrolink.product.response;

import java.time.YearMonth;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductDetailResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean hidden,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductCategory productCategory,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  MainIngredientRegion mainIngredientRegion,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String contentQuantity,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductExpirationType productExpirationType,

  Short shelfLifeDays,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  StorageType storageType,

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
  String mainImageUrl,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<MonthlySupplyCapacity> monthlySupplyCapacities,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductStory> productStories,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Supplier supplier,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Permissions permissions,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean hasMyActiveNegotiationRequest
) {

  public record ProductCategory(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name
  ) {}

  public record MainIngredientRegion(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name
  ) {}

  public record ProductExpirationType(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    ProductExpirationType value,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String label
  ) {}

  public record StorageType(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    StorageType value,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String label
  ) {}

  public record MonthlySupplyCapacity(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    YearMonth targetMonth,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer availableQuantity
  ) {}

  public record ProductStory(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short productStorySectionTemplateId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short position,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String title,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String body,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String imageUrl
  ) {}

  public record Supplier(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String businessName,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String businessAddressPrefecture,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String businessAddressMunicipalityStreet,

    String businessAddressBuilding,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String businessWebsiteUrl
  ) {}

  public record Permissions(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Boolean canManage,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Boolean canCreateNegotiationRequest
  ) {}
}
