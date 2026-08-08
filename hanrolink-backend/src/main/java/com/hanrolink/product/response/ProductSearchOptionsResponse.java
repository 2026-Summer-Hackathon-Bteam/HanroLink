package com.hanrolink.product.response;

import java.util.List;
import java.util.Objects;

import com.hanrolink.product.response.component.StorageTypeOptionResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryGroupOptionResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryOptionResponse;
import com.hanrolink.region.response.component.RegionOptionResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductSearchOptionsResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryGroupOptionResponse> productCategoryGroups,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryOptionResponse> productCategories,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<RegionOptionResponse> mainIngredientRegions,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<StorageTypeOptionResponse> storageTypes
) {
  public ProductSearchOptionsResponse {
    Objects.requireNonNull(
      productCategoryGroups,
      "ProductSearchOptionsResponse.productCategoryGroups must not be null"
    );

    Objects.requireNonNull(
      productCategories,
      "ProductSearchOptionsResponse.productCategories must not be null"
    );

    Objects.requireNonNull(
      mainIngredientRegions,
      "ProductSearchOptionsResponse.mainIngredientRegions must not be null"
    );

    Objects.requireNonNull(
      storageTypes,
      "ProductSearchOptionsResponse.storageTypes must not be null"
    );
  }
}
