package com.hanrolink.negotiationrequest.repository.projection;

import java.time.Instant;
import java.util.UUID;

public record SupplierSentNegotiationRequestListProjection(
  UUID publicId,
  UUID procurementRequestPublicId,
  String procurementRequestTitle,
  UUID productPublicId,
  String productName,
  Instant createdAt
) {}
