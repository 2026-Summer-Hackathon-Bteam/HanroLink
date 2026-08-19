package com.hanrolink.negotiationrequest.repository.projection;

import java.time.Instant;
import java.util.UUID;

public record BuyerProductNegotiationRequestListProjection(
  UUID publicId,
  UUID productPublicId,
  String productName,
  Instant createdAt
) {}
