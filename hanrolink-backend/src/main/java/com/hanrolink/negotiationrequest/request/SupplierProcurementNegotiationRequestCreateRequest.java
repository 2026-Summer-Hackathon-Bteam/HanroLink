package com.hanrolink.negotiationrequest.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SupplierProcurementNegotiationRequestCreateRequest(
  @NotNull
  @Positive
  UUID productId
) {}
