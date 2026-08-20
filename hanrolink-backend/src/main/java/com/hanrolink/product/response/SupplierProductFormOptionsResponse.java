package com.hanrolink.product.response;

import java.util.List;
import java.util.Objects;

import com.hanrolink.product.response.component.ProductExpirationTypeOptionResponse;
import com.hanrolink.product.response.component.ProductStorySectionTemplateOptionResponse;
import com.hanrolink.product.response.component.StorageTypeOptionResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryGroupOptionResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryOptionResponse;
import com.hanrolink.region.response.component.PrefectureOptionResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductFormOptionsResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryGroupOptionResponse> productCategoryGroups,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryOptionResponse> productCategories,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<PrefectureOptionResponse> mainIngredientOriginPrefectures,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductExpirationTypeOptionResponse> productExpirationTypes,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<StorageTypeOptionResponse> storageTypes,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductStorySectionTemplateOptionResponse> productStorySectionTemplates
) {
  public SupplierProductFormOptionsResponse {
    Objects.requireNonNull(
      productCategoryGroups,
      "SupplierProductFormOptionsResponse.productCategoryGroups must not be null"
    );

    Objects.requireNonNull(
      productCategories,
      "SupplierProductFormOptionsResponse.productCategories must not be null"
    );

    Objects.requireNonNull(
      mainIngredientOriginPrefectures,
      "SupplierProductFormOptionsResponse.mainIngredientOriginPrefectures must not be null"
    );

    Objects.requireNonNull(
      productExpirationTypes,
      "SupplierProductFormOptionsResponse.productExpirationTypes must not be null"
    );

    Objects.requireNonNull(
      storageTypes,
      "SupplierProductFormOptionsResponse.storageTypes must not be null"
    );

    Objects.requireNonNull(
      productStorySectionTemplates,
      "SupplierProductFormOptionsResponse.productStorySectionTemplates must not be null"
    );
  }
}
