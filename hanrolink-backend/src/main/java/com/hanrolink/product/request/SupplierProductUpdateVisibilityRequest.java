package com.hanrolink.product.request;

import jakarta.validation.constraints.NotNull;

public record SupplierProductUpdateVisibilityRequest(
  @NotNull
  Boolean hidden
) {}
