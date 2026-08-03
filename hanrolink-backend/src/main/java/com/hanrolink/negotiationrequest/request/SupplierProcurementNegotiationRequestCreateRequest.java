package com.hanrolink.negotiationrequest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SupplierProcurementNegotiationRequestCreateRequest(
  @NotNull
  @Positive
  Long productId
) {}
