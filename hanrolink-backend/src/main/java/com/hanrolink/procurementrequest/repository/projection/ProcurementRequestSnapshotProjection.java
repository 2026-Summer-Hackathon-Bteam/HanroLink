package com.hanrolink.procurementrequest.repository.projection;

import java.time.Instant;

public record ProcurementRequestSnapshotProjection(
  Instant updatedAt,
  Long id,
  Short productCategoryId,
  String productCategoryName,
  String title,
  String description,
  String requiredTradeTerms,
  Integer desiredUnitPrice,
  Short deliveryShelfLifeDays
) {}
