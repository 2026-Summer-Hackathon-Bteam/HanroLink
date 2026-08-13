package com.hanrolink.product.request;

import com.hanrolink.file.policy.ImageFilePolicy;
import com.hanrolink.product.enums.ProductImageUsage;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SupplierProductImageUploadCreateRequest(
  @NotNull
  ProductImageUsage usage,

  @NotNull
  @Positive
  @Max(
    value = ImageFilePolicy.MAX_FILE_SIZE_BYTES,
    message = "画像の容量が上限を超えています"
  )
  Long fileSizeBytes
) {}
