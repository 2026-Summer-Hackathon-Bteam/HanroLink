package com.hanrolink.chat.response;

import java.time.Instant;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyChatMessageListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Long id,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String senderBusinessName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  boolean isMine,

  String body,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Instant createdAt,

  List<MessageFile> messageFiles
) {

  public record MessageFile(
    String displayFilename,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String url,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    String mimeType,

    Long fileSizeBytes
  ) {}
}
