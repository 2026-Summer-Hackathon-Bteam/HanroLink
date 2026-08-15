package com.hanrolink.business.repository.projection;

import java.time.Instant;
import java.util.UUID;

public record AdminBusinessApprovalListProjection(
  UUID businessId,
  String businessName,
  Instant createdAt
) {}
