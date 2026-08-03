package com.hanrolink.product.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hanrolink.product.enums.ProductExpirationType;
import com.hanrolink.product.enums.StorageType;
import com.hanrolink.product.request.component.MonthlySupplyCapacityRequest;
import com.hanrolink.product.request.component.ProductStoryCreateRequest;
import com.hanrolink.web.validation.NotEmptyFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SupplierProductCreateRequest(
  @NotBlank
  @Size(max = 255)
  String name,

  @NotNull
  @Positive
  Short productCategoryId,

  @NotNull
  @Positive
  Short mainIngredientRegionId,

  @NotNull
  @NotEmptyFile
  MultipartFile mainImageFile,

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
  @Size(min = 6, max = 6, message = "6か月分指定してください")
  @Valid
  List<@NotNull MonthlySupplyCapacityRequest> monthlySupplyCapacities,

  @NotNull
  @Size(min = 4, max = 4, message = "4つすべて入力してください")
  @Valid
  List<@NotNull ProductStoryCreateRequest> productStories
) {}
