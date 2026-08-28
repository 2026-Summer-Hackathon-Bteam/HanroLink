package com.hanrolink.chat.repository.projection;

public record ChatMessageFileProjection(
  Long messageId,
  String displayFilename,
  String storageKey,
  Long fileSizeBytes
) {}
