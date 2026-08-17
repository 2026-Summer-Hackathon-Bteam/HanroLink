package com.hanrolink.procurementrequest.repository.projection;

import java.time.LocalDate;

public record ProcurementRequestSearchMonthlyProcurementQuantityProjection(
  Long procurementRequestId,
  LocalDate targetMonth,
  Integer desiredQuantity
) {}
