package com.hanrolink.procurementrequest.repository.projection;

import java.time.Instant;
import java.util.UUID;

public record BuyerProcurementRequestListProjection(
  UUID publicId,
  String title,
  Instant updatedAt
) {}
