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
import com.hanrolink.product.policy.ProductPolicy;
import com.hanrolink.product.policy.ProductStoryPolicy;
import com.hanrolink.product.request.component.MonthlySupplyCapacityRequest;
import com.hanrolink.product.request.component.ProductStoryUpdateRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record SupplierProductUpdateRequest(
  @NotBlank
  @Size(max = ProductPolicy.MAX_NAME_LENGTH)
  String name,

  @NotNull
  @Positive
  Short productCategoryId,

  @NotNull
  @Positive
  Short mainIngredientOriginPrefectureId,

  UUID mainImagePendingFileUploadId,

  @NotBlank
  @Size(max = ProductPolicy.MAX_CONTENT_QUANTITY_LENGTH)
  String contentQuantity,

  @NotNull
  ProductExpirationType expirationType,

  @PositiveOrZero
  Short shelfLifeDays,

  @NotNull
  StorageType storageType,

  @NotNull
  @Positive
  Integer desiredRetailPrice,

  @Size(max = ProductPolicy.MAX_ALLERGY_INFORMATION_LENGTH)
  String allergyInformation,

  @Size(
    max = ProductPolicy.MAX_CERTIFICATION_INFORMATION_LENGTH
  )
  String certificationInformation,

  @Size(max = ProductPolicy.MAX_CASE_SIZE_LENGTH)
  String caseSize,

  @Positive
  Integer unitsPerCase,

  @Positive
  Integer minimumOrderQuantity,

  @PositiveOrZero
  Short shippingLeadTimeDays,

  @Size(max = ProductPolicy.MAX_SALES_AREA_RESTRICTION_LENGTH)
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
        monthlySupplyCapacity -> monthlySupplyCapacity == null
        || monthlySupplyCapacity.targetMonth() == null
      )
    ) {
      return true;
    }

    List<YearMonth> supplyMonths =
      monthlySupplyCapacities
        .stream()
        .map(monthlySupplyCapacity -> monthlySupplyCapacity.targetMonth())
        .sorted()
        .toList();

    YearMonth currentMonth = YearMonth.now(ZoneId.of("Asia/Tokyo"));

    return IntStream
      .range(0, MonthlySupplyCapacityPolicy.TARGET_MONTH_COUNT)
      .allMatch(monthsAfterCurrentMonth ->
        supplyMonths
          .get(monthsAfterCurrentMonth)
          .equals(currentMonth.plusMonths(monthsAfterCurrentMonth))
      );
  }

  @AssertTrue(message = "重複しない位置を指定してください")
  public boolean hasUniqueStoryPositions() {
    if (productStories == null
      || productStories.size() != ProductStoryPolicy.REQUIRED_COUNT
      || productStories.stream().anyMatch(
        productStory -> productStory == null
        || productStory.position() == null
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

  @AssertTrue(message = "同じ商品ストーリーのタイトルを複数指定することはできません")
  public boolean hasUniqueStorySectionTemplateIds() {
    if (productStories == null
      || productStories.size() != ProductStoryPolicy.REQUIRED_COUNT
      || productStories.stream().anyMatch(
        productStory -> productStory == null
        || productStory.productStorySectionTemplateId() == null
      )
    ) {
      return true;
    }

    return productStories
      .stream()
      .map(productStory ->
        productStory.productStorySectionTemplateId()
      )
      .distinct()
      .count() == productStories.size();
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