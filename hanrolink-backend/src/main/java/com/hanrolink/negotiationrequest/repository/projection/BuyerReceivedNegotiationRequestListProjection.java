package com.hanrolink.negotiationrequest.repository.projection;

import java.time.Instant;
import java.util.UUID;

public record BuyerReceivedNegotiationRequestListProjection(
  UUID publicId,
  UUID procurementRequestPublicId,
  String procurementRequestTitle,
  UUID productPublicId,
  String productName,
  String senderBusinessName,
  Instant expiresAt
) {}
