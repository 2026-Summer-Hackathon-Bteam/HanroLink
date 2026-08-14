package com.hanrolink.product.repository.projection;

import java.time.LocalDate;

public record ProductSearchMonthlySupplyCapacityProjection(
  Long productId,
  LocalDate targetMonth,
  Integer availableQuantity
) {}
