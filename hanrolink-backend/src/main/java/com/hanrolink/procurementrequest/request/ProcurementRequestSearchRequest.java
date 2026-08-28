package com.hanrolink.procurementrequest.request;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.hanrolink.procurementrequest.policy.MonthlyProcurementQuantityPolicy;
import com.hanrolink.product.enums.StorageType;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProcurementRequestSearchRequest(
  @DateTimeFormat(pattern = "yyyy-MM")
  List<
    @NotNull
    @FutureOrPresent(message = "当月以降の月を指定してください")
    YearMonth
  > desiredProcurementMonths,

  List<
    @NotNull
    @Positive
    Short
  > productCategoryIds,

  Set<@NotNull StorageType> storageTypes,

  @Size(max = 255)
  String keyword,

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
  @Max(50)
  Integer pageSize
) {
  public ProcurementRequestSearchRequest {
    page = Objects.requireNonNullElse(page, 1);
    pageSize = Objects.requireNonNullElse(pageSize, 20);
  }

  @AssertTrue(message = "当月から" + MonthlyProcurementQuantityPolicy.TARGET_MONTH_COUNT + "か月分の範囲内で指定してください")
  public boolean hasDesiredProcurementMonths() {
    if (desiredProcurementMonths == null
      || desiredProcurementMonths.isEmpty()
      || desiredProcurementMonths.stream().anyMatch(
        month -> month == null
      )
    ) {
      return true;
    }

    YearMonth lastDesiredMonth =
      YearMonth
        .now(ZoneId.of("Asia/Tokyo"))
        .plusMonths(
          MonthlyProcurementQuantityPolicy.TARGET_MONTH_COUNT - 1
        );

    return desiredProcurementMonths
      .stream()
      .allMatch(month ->
        !month.isAfter(lastDesiredMonth)
      );
  }
}
