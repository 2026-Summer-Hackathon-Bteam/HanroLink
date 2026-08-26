package com.hanrolink.procurementrequest.request;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import com.hanrolink.procurementrequest.policy.MonthlyProcurementQuantityPolicy;
import com.hanrolink.procurementrequest.policy.ProcurementRequestPolicy;
import com.hanrolink.procurementrequest.request.component.MonthlyProcurementQuantityRequest;
import com.hanrolink.product.enums.StorageType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record BuyerProcurementRequestCreateRequest(
  @NotBlank
  @Size(max = ProcurementRequestPolicy.MAX_TITLE_LENGTH)
  String title,

  @NotBlank
  @Size(
    max = ProcurementRequestPolicy.MAX_DESCRIPTION_LENGTH
  )
  String description,

  @NotNull
  @Positive
  Short productCategoryId,

  @Size(
    max = ProcurementRequestPolicy.MAX_REQUIRED_TRADE_TERMS_LENGTH
  )
  String requiredTradeTerms,

  @Positive
  Integer desiredUnitPrice,

  @Positive
  Short deliveryShelfLifeDays,

  @NotEmpty
  Set<@NotNull StorageType> storageTypes,

  @NotNull
  @Size(
    min = MonthlyProcurementQuantityPolicy.TARGET_MONTH_COUNT,
    max = MonthlyProcurementQuantityPolicy.TARGET_MONTH_COUNT,
    message = "{min}か月分指定してください"
  )
  List<@NotNull @Valid MonthlyProcurementQuantityRequest> monthlyProcurementQuantities
) {
  @AssertTrue(message = "当月から連続する" + MonthlyProcurementQuantityPolicy.TARGET_MONTH_COUNT + "か月を指定してください")
  public boolean hasConsecutiveProcurementMonthsFromCurrentMonth() {
    if (monthlyProcurementQuantities == null
      || monthlyProcurementQuantities.size() != MonthlyProcurementQuantityPolicy.TARGET_MONTH_COUNT
      || monthlyProcurementQuantities.stream().anyMatch(
        monthlyProcurementQuantity -> monthlyProcurementQuantity == null
        || monthlyProcurementQuantity.targetMonth() == null
      )
    ) {
      return true;
    }

    List<YearMonth> procurementMonths =
      monthlyProcurementQuantities
        .stream()
        .map(monthlyProcurementQuantity ->
          monthlyProcurementQuantity.targetMonth()
        )
        .sorted()
        .toList();

    YearMonth currentMonth = YearMonth.now(ZoneId.of("Asia/Tokyo"));

    return IntStream
      .range(0, MonthlyProcurementQuantityPolicy.TARGET_MONTH_COUNT)
      .allMatch(monthsAfterCurrentMonth ->
        procurementMonths
          .get(monthsAfterCurrentMonth)
          .equals(currentMonth.plusMonths(monthsAfterCurrentMonth))
      );
  }
}
