package com.hanrolink.product.request;

import com.hanrolink.product.enums.ProductImageUsage;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SupplierProductImageUploadCreateRequest(
  @NotNull
  ProductImageUsage usage,

  @NotNull
  @Positive
  Long fileSizeBytes
) {}
