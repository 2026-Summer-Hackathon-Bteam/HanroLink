package com.hanrolink.product.response;

import java.util.List;

import com.hanrolink.product.response.component.ProductExpirationTypeOptionResponse;
import com.hanrolink.product.response.component.ProductStorySectionTemplateOptionResponse;
import com.hanrolink.product.response.component.StorageTypeOptionResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryGroupOptionResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryOptionResponse;
import com.hanrolink.region.response.component.RegionOptionResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductFormOptionsResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryGroupOptionResponse> productCategoryGroups,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryOptionResponse> productCategories,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<RegionOptionResponse> mainIngredientRegions,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductExpirationTypeOptionResponse> productExpirationTypes,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<StorageTypeOptionResponse> storageTypes,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductStorySectionTemplateOptionResponse> productStorySectionTemplates
) {}
