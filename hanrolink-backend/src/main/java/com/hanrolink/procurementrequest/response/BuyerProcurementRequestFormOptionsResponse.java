package com.hanrolink.procurementrequest.response;

import java.util.List;
import java.util.Objects;

import com.hanrolink.product.response.component.StorageTypeOptionResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryGroupOptionResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryOptionResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementRequestFormOptionsResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryGroupOptionResponse> productCategoryGroups,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryOptionResponse> productCategories,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<StorageTypeOptionResponse> storageTypes
) {
  public BuyerProcurementRequestFormOptionsResponse {
    Objects.requireNonNull(
      productCategoryGroups,
      "BuyerProcurementRequestFormOptionsResponse.productCategoryGroups must not be null"
    );

    Objects.requireNonNull(
      productCategories,
      "BuyerProcurementRequestFormOptionsResponse.productCategories must not be null"
    );

    Objects.requireNonNull(
      storageTypes,
      "BuyerProcurementRequestFormOptionsResponse.storageTypes must not be null"
    );
  }
}
