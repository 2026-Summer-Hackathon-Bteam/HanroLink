package com.hanrolink.procurementrequest.response;

import java.util.List;

import com.hanrolink.product.enums.StorageType;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestSearchOptionsResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryGroupOption> productCategoryGroups,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryOption> productCategories,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<StorageTypeOption> storageTypes
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

  public record StorageTypeOption(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    StorageType value,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String label
  ) {}
}
