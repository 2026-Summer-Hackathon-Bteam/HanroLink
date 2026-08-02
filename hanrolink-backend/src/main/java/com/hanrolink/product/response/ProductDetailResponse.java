package com.hanrolink.product.response;

import java.util.List;

import com.hanrolink.product.response.component.MonthlySupplyCapacityResponse;
import com.hanrolink.product.response.component.ProductExpirationTypeResponse;
import com.hanrolink.product.response.component.ProductMainIngredientRegionResponse;
import com.hanrolink.product.response.component.ProductPermissionsResponse;
import com.hanrolink.product.response.component.ProductStoryResponse;
import com.hanrolink.product.response.component.ProductSupplierResponse;
import com.hanrolink.product.response.component.StorageTypeResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductDetailResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String name,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean hidden,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductCategoryResponse productCategory,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductMainIngredientRegionResponse mainIngredientRegion,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String contentQuantity,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductExpirationTypeResponse productExpirationType,

  Short shelfLifeDays,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  StorageTypeResponse storageType,

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
  List<MonthlySupplyCapacityResponse> monthlySupplyCapacities,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductStoryResponse> productStories,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductSupplierResponse supplier,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductPermissionsResponse permissions,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean hasMyActiveNegotiationRequest
) {}
