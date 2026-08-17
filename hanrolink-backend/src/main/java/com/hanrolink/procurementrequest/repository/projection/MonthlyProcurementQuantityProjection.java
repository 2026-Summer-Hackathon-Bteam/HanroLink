package com.hanrolink.procurementrequest.repository.projection;

import java.time.LocalDate;

public record MonthlyProcurementQuantityProjection(
  LocalDate targetMonth,
  Integer desiredQuantity
) {}
