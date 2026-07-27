package com.hanrolink.procurementrequest.response;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

import com.hanrolink.productcategory.entity.ProductCategory;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestDetailResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String title,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String description,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Buyer buyer,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductCategory productCategory,

  String requiredTradeTerms,

  Integer desiredUnitPrice,

  Short deliveryShelfLifeDays,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<StorageType> storageTypes,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<MonthlyProcurementQuantity> monthlyProcurementQuantities,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Permissions permissions,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean hasMyActiveNegotiationRequest
) {

  public record Buyer(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    UUID accountId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String businessName
  ) {}

  public record productCategory(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Short id,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String name
  ) {}

  public record StorageType(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    StorageType value,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String label
  ) {}

  public record MonthlyProcurementQuantity(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    YearMonth targetMonth,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Integer desiredQuantity
  ) {}

  public record Permissions(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Boolean canManage,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Boolean canCreateNegotiationRequest
  ) {}
}
