package com.hanrolink.product.request;

import java.util.List;
import java.util.UUID;

import com.hanrolink.product.enums.ProductExpirationType;
import com.hanrolink.product.enums.StorageType;
import com.hanrolink.product.policy.MonthlySupplyCapacityPolicy;
import com.hanrolink.product.policy.ProductStoryPolicy;
import com.hanrolink.product.request.component.MonthlySupplyCapacityRequest;
import com.hanrolink.product.request.component.ProductStoryUpdateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SupplierProductUpdateRequest(
  @NotBlank
  @Size(max = 255)
  String name,

  @NotNull
  @Positive
  Short productCategoryId,

  @NotNull
  @Positive
  Short mainIngredientRegionId,

  UUID mainImagePendingFileUploadId,

  @NotBlank
  @Size(max = 255)
  String contentQuantity,

  @NotNull
  ProductExpirationType expirationType,

  @Positive
  Short shelfLifeDays,

  @NotNull
  StorageType storageType,

  @NotNull
  @Positive
  Integer desiredRetailPrice,

  @Size(max = 255)
  String allergyInformation,

  String certificationInformation,

  @Size(max = 255)
  String caseSize,

  @Positive
  Integer unitsPerCase,

  @Positive
  Integer minimumOrderQuantity,

  @Positive
  Short shippingLeadTimeDays,

  @Size(max = 255)
  String salesAreaRestriction,

  @NotNull
  @Size(
    min = MonthlySupplyCapacityPolicy.TARGET_MONTH_COUNT,
    max = MonthlySupplyCapacityPolicy.TARGET_MONTH_COUNT,
    message = "{min}か月分指定してください"
  )
  @Valid
  List<@NotNull MonthlySupplyCapacityRequest> monthlySupplyCapacities,

  @NotNull
  @Size(
    min = ProductStoryPolicy.REQUIRED_COUNT,
    max = ProductStoryPolicy.REQUIRED_COUNT,
    message = "{min}つすべて入力してください"
  )
  @Valid
  List<@NotNull ProductStoryUpdateRequest> productStories
) {}