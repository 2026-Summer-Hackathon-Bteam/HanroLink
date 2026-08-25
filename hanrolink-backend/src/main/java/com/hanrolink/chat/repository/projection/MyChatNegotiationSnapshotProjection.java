package com.hanrolink.chat.repository.projection;

import com.hanrolink.chat.enums.NegotiationTargetType;
import com.hanrolink.negotiationrequest.snapshot.ProcurementRequestSnapshot;
import com.hanrolink.negotiationrequest.snapshot.ProductSnapshot;

public record MyChatNegotiationSnapshotProjection(
  NegotiationTargetType negotiationTargetType,
  ProductSnapshot requestedProductSnapshot,
  ProductSnapshot acceptedProductSnapshot,
  ProcurementRequestSnapshot requestedProcurementRequestSnapshot,
  ProcurementRequestSnapshot acceptedProcurementRequestSnapshot
) {}
