package com.hanrolink.chat.response;

import java.util.Objects;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyChatFileUploadCreateResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String uploadUrl,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  UUID pendingFileUploadId
) {
  public MyChatFileUploadCreateResponse {
    Objects.requireNonNull(
      uploadUrl,
      "MyChatFileUploadCreateResponse.uploadUrl must not be null"
    );

    Objects.requireNonNull(
      pendingFileUploadId,
      "MyChatFileUploadCreateResponse.pendingFileUploadId must not be null"
    );
  }
}
