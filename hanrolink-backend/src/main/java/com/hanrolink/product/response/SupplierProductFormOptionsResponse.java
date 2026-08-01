package com.hanrolink.product.response;

import java.util.List;

import com.hanrolink.product.enums.ProductExpirationType;
import com.hanrolink.product.enums.StorageType;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductFormOptionsResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryGroupOption> productCategoryGroups,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryOption> productCategories,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<RegionOption> mainIngredientRegions,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductExpirationTypeOption> productExpirationTypes,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<StorageTypeOption> storageTypes,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductStorySectionTemplateOption> productStorySectionTemplates
) {

  public record ProductCategoryGroupOption(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short sortOrder
  ) {}

  public record ProductCategoryOption(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short productCategoryGroupId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short sortOrder
  ) {}

  public record RegionOption(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short sortOrder
  ) {}

  public record ProductExpirationTypeOption(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    ProductExpirationType value,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String label
  ) {}

  public record StorageTypeOption(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    StorageType value,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String label
  ) {}

  public record ProductStorySectionTemplateOption(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String title,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String imageHint,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String bodyHelpText,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String bodyExample,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short sortOrder
  ) {}
}
