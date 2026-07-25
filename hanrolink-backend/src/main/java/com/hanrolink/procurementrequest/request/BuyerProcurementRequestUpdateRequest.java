package com.hanrolink.procurementrequest.request;

import java.time.YearMonth;
import java.util.List;
import java.util.Set;

import com.hanrolink.product.enums.StorageType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record BuyerProcurementRequestUpdateRequest(
  @NotBlank
  @Size(max = 255)
  String title,

  @NotBlank
  String description,

  @NotNull
  @Positive
  Short productCategoryId,

  String requiredTradeTerms,

  @Positive
  Integer desiredUnitPrice,

  @Positive
  Short deliveryShelfLifeDays,

  @NotEmpty
  Set<@NotNull StorageType> storageTypes,

  @NotNull
  @Size(min = 6, max = 6, message = "6か月分指定してください")
  List<@NotNull @Valid MonthlyRequirement> monthlyRequirements
) {

  public record MonthlyRequirement(
    @NotNull
    @FutureOrPresent(message = "当月以降の月を指定してください")
    YearMonth targetMonth,

    @NotNull
    @Positive
    Integer desiredQuantity
  ) {}
}