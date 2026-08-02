package com.hanrolink.chat.response.component;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChatMessageFileResponse(
  String displayFilename,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String url,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String mimeType,

  Long fileSizeBytes
) {}
