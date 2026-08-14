package com.hanrolink.product.request;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

import com.hanrolink.product.enums.ProductExpirationType;
import com.hanrolink.product.enums.StorageType;
import com.hanrolink.product.policy.MonthlySupplyCapacityPolicy;
import com.hanrolink.product.policy.ProductStoryPolicy;
import com.hanrolink.product.request.component.MonthlySupplyCapacityRequest;
import com.hanrolink.product.request.component.ProductStoryUpdateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
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
) {
  @AssertTrue(message = "当月から連続する" + MonthlySupplyCapacityPolicy.TARGET_MONTH_COUNT + "か月を指定してください")
  public boolean hasConsecutiveSupplyMonthsFromCurrentMonth() {
    if (monthlySupplyCapacities == null
      || monthlySupplyCapacities.size() != MonthlySupplyCapacityPolicy.TARGET_MONTH_COUNT
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
      .range(0, MonthlySupplyCapacityPolicy.TARGET_MONTH_COUNT)
      .allMatch(monthOffset ->
        supplyMonths
          .get(monthOffset)
          .equals(currentMonth.plusMonths(monthOffset))
      );
  }

  @AssertTrue(message = "重複しない位置を指定してください")
  public boolean hasUniqueStoryPositions() {
    if (productStories == null
      || productStories.size() != ProductStoryPolicy.REQUIRED_COUNT
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
      .count() == ProductStoryPolicy.REQUIRED_COUNT;
  }

  @AssertTrue(message = "同じ画像を複数指定することはできません")
  public boolean hasUniquePendingFileUploadIds() {
    if (productStories == null
      || productStories.size() != ProductStoryPolicy.REQUIRED_COUNT
      || productStories.stream().anyMatch(Objects::isNull)
    ) {
      return true;
    }

    List<UUID> pendingFileUploadIds =
      productStories
        .stream()
        .map(productStory ->
          productStory.pendingFileUploadId()
        )
        .filter(Objects::nonNull)
        .toList();

    return pendingFileUploadIds
      .stream()
      .distinct()
      .count() == pendingFileUploadIds.size();
  }
}