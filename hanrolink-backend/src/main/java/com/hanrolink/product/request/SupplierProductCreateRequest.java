package com.hanrolink.product.request;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.web.multipart.MultipartFile;

import com.hanrolink.product.enums.ProductExpirationType;
import com.hanrolink.product.enums.StorageType;
import com.hanrolink.product.request.component.MonthlySupplyCapacityRequest;
import com.hanrolink.product.request.component.ProductStoryCreateRequest;
import com.hanrolink.web.validation.NotEmptyFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
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
) {
  @AssertTrue(message = "当月から連続する6か月を指定してください")
  public boolean hasSixSupplyMonthsFromCurrentMonth() {
    if (monthlySupplyCapacities == null
      || monthlySupplyCapacities.size() != 6
      || monthlySupplyCapacities.stream().anyMatch(
        item -> item == null || item.targetMonth() == null
      )
    ) {
      return true;
    }

    List<YearMonth> supplyMonths =
      monthlySupplyCapacities
        .stream()
        .map(item -> item.targetMonth())
        .sorted()
        .toList();

    YearMonth currentMonth = YearMonth.now(ZoneId.of("Asia/Tokyo"));

    return IntStream
      .range(0, 6)
      .allMatch(monthOffset ->
        supplyMonths
          .get(monthOffset)
          .equals(currentMonth.plusMonths(monthOffset))
      );
  }

  @AssertTrue(message = "重複しない位置を指定してください")
  public boolean hasUniqueStoryPositions() {
    if (productStories == null
      || productStories.size() != 4
      || productStories.stream().anyMatch(
        item -> item == null || item.position() == null
      ) 
    ) {
      return true;
    }

    return productStories
      .stream()
      .map(productStory ->
        productStory.position()
      )
      .distinct()
      .count() == 4;
  }
}
