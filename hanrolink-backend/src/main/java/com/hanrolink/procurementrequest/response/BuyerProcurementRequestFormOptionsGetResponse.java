package com.hanrolink.procurementrequest.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementRequestFormOptionsGetResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ProductCategoryOption> productCategories,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<StorageTypeOption> storageTypes
) {

  public record ProductCategoryOption(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String label
  ) {}

  public record StorageTypeOption(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String value,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String label
  ) {}
}
