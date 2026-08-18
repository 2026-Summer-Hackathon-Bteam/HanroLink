package com.hanrolink.product.request;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.hanrolink.product.enums.StorageType;
import com.hanrolink.product.policy.MonthlySupplyCapacityPolicy;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductSearchRequest(
  @DateTimeFormat(pattern = "yyyy-MM")
  List<
    @NotNull
    @FutureOrPresent(message = "当月以降の月を指定してください")
    YearMonth
  > availableSupplyMonths,

  List<
    @NotNull
    @Positive
    Short
  > mainIngredientOriginRegionIds,

  List<
    @NotNull
    @Positive
    Short
  > productCategoryIds,

  Set<StorageType> storageTypes,

  @Parameter(
    schema = @Schema(
      type = "integer",
      format = "int32",
      defaultValue = "1"
    )
  )
  @Min(1)
  Integer page,

  @Parameter(
    schema = @Schema(
      type = "integer",
      format = "int32",
      defaultValue = "20"
    )
  )
  @Min(1)
  @Max(100)
  Integer pageSize
) {
  public ProductSearchRequest {
    page = Objects.requireNonNullElse(page, 1);
    pageSize = Objects.requireNonNullElse(pageSize, 20);
  }

  @AssertTrue(message = "当月から" + MonthlySupplyCapacityPolicy.TARGET_MONTH_COUNT + "か月分の範囲内で指定してください")
  public boolean hasSupplyMonthsWithinTargetPeriod() {
    if (availableSupplyMonths == null
      || availableSupplyMonths.isEmpty()
      || availableSupplyMonths.stream().anyMatch(
        month -> month == null
      )
    ) {
      return true;
    }

    YearMonth lastAvailableMonth =
      YearMonth
        .now(ZoneId.of("Asia/Tokyo"))
        .plusMonths(
          MonthlySupplyCapacityPolicy.TARGET_MONTH_COUNT - 1
        );

    return availableSupplyMonths
      .stream()
      .allMatch(month ->
        !month.isAfter(lastAvailableMonth)
      );
  }
}
