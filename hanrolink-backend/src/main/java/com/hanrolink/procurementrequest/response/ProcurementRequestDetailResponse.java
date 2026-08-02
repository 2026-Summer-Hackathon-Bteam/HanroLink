package com.hanrolink.procurementrequest.response;

import java.util.List;

import com.hanrolink.procurementrequest.response.component.MonthlyProcurementQuantityResponse;
import com.hanrolink.procurementrequest.response.component.ProcurementRequestBuyerResponse;
import com.hanrolink.procurementrequest.response.component.ProcurementRequestPermissionsResponse;
import com.hanrolink.product.response.component.StorageTypeResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProcurementRequestDetailResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String title,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String description,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProcurementRequestBuyerResponse buyer,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProductCategoryResponse productCategory,

  String requiredTradeTerms,

  Integer desiredUnitPrice,

  Short deliveryShelfLifeDays,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<StorageTypeResponse> storageTypes,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<MonthlyProcurementQuantityResponse> monthlyProcurementQuantities,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ProcurementRequestPermissionsResponse permissions,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean hasMyActiveNegotiationRequest
) {}
