package com.hanrolink.product.request;

import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.hanrolink.product.enums.StorageType;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record ProductSearchRequest(
  @DateTimeFormat(pattern = "yyyy-MM")
  List<
    @FutureOrPresent(message = "当月以降の月を指定してください")
    YearMonth
  > availableSupplyMonths,

  List<@Positive Short> mainIngredientRegionIds,

  List<@Positive Short> productCategoryGroupIds,

  List<@Positive Short> productCategoryIds,

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
}
