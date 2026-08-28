package com.hanrolink.product.repository.projection;

import java.time.LocalDate;

public record MonthlySupplyCapacityProjection(
  LocalDate targetMonth,
  Integer availableQuantity
) {}
