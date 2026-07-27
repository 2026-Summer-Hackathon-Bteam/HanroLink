package com.hanrolink.product.request;

import java.time.YearMonth;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.hanrolink.product.enums.ProductExpirationType;
import com.hanrolink.product.enums.StorageType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

  String allergyInformation,

  String certificationInformation,

  String caseSize,

  @Positive
  Integer unitsPerCase,

  @Positive
  Integer minimumOrderQuantity,

  @Positive
  Short shippingLeadTimeDays,

  String salesAreaRestriction,

  @NotNull
  @Size(min = 6, max = 6, message = "6か月分指定してください")
  @Valid
  List<@NotNull MonthlySupplyCapacity> monthlySupplyCapacities,

  @NotNull
  @Size(min = 4, max = 4, message = "4つすべて入力してください")
  @Valid
  List<@NotNull ProductStory> productStories
) {

  public record MonthlySupplyCapacity(
    @NotNull
    @FutureOrPresent(message = "当月以降の月を指定してください")
    @DateTimeFormat(pattern = "yyyy-MM")
    YearMonth targetMonth,

    @NotNull
    @Positive
    Integer availableQuantity
  ) {}

  public record ProductStory(
    @NotNull
    @Positive
    Long id,

    @NotNull
    @Positive
    Short position,

    @NotNull
    @Min(1)
    @Max(4)
    Short productStorySectionTemplateId,

    @NotBlank
    String body,

    MultipartFile imageFile
  ) {}
}