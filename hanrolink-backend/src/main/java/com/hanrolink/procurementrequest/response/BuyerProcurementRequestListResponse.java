package com.hanrolink.procurementrequest.response;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record BuyerProcurementRequestListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String title,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String description,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String productCategoryName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<StorageType> storageTypeLabels,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<MonthlyRequirement> monthlyRequirements,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Buyer buyer
) {

  public record StorageType(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String label
  ) {}

  public record MonthlyRequirement(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    YearMonth targetMonth,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer quantity
  ) {}

  public record Buyer(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    UUID accountId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String businessName
  ) {}
}
