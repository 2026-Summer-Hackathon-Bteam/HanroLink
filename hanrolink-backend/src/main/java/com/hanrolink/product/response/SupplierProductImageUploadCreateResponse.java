package com.hanrolink.product.response;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupplierProductImageUploadCreateResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String uploadUrl,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID pendingFileUploadId
) {
  public SupplierProductImageUploadCreateResponse {
    Objects.requireNonNull(
      uploadUrl,
      "SupplierProductImageUploadCreateResponse.uploadUrl must not be null"
    );

    Objects.requireNonNull(
      uploadUrl,
      "SupplierProductImageUploadCreateResponse.pendingFileUploadId must not be null"
    );
  }
}
