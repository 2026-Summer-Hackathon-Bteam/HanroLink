package com.hanrolink.chat.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatMessageFileResponse(
  String displayFilename,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String url,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String mimeType,

  Long fileSizeBytes
) {
  public ChatMessageFileResponse {
    Objects.requireNonNull(
      url,
      "ChatMessageFileResponse.url must not be null"
    );

    Objects.requireNonNull(
      mimeType,
      "ChatMessageFileResponse.mimeType must not be null"
    );
  }
}
