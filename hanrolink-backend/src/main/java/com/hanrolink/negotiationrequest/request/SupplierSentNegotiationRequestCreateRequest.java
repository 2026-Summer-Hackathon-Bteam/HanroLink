package com.hanrolink.negotiationrequest.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record SupplierSentNegotiationRequestCreateRequest(
  @NotNull
  UUID productId
) {}
