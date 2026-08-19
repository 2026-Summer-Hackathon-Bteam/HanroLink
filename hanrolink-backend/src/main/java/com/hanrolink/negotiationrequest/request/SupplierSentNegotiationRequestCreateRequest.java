package com.hanrolink.negotiationrequest.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SupplierSentNegotiationRequestCreateRequest(
  @NotNull
  @Positive
  UUID productId
) {}
