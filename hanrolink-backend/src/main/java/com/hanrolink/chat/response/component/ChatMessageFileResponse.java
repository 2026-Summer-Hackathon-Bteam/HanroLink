package com.hanrolink.chat.response.component;

import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatMessageFileResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String displayFilename,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String url,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long fileSizeBytes
) {
  public ChatMessageFileResponse {
    Objects.requireNonNull(
      displayFilename,
      "ChatMessageFileResponse.displayFilename must not be null"
    );

    Objects.requireNonNull(
      url,
      "ChatMessageFileResponse.url must not be null"
    );

    Objects.requireNonNull(
      fileSizeBytes,
      "ChatMessageFileResponse.fileSizeBytes must not be null"
    );
  }
}
