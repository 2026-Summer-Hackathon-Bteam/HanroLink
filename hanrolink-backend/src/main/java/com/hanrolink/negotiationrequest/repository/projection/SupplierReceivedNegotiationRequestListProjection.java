package com.hanrolink.negotiationrequest.repository.projection;

import java.time.Instant;
import java.util.UUID;

public record SupplierReceivedNegotiationRequestListProjection(
  UUID publicId,
  UUID productPublicId,
  String productName,
  UUID senderBusinessPublicId,
  String senderBusinessName,
  Instant createdAt
) {}
