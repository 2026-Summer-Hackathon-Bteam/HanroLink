package com.hanrolink.chat.request;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.util.StringUtils;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record MyChatMessageCreateRequest(
  String body,

  List<@NotNull UUID> pendingFileUploadIds
) {
  @AssertTrue(message = "本文またはファイルのどちらかは必須です")
  public boolean isContentPresent() {
    return StringUtils.hasText(body) || (pendingFileUploadIds != null && !pendingFileUploadIds.isEmpty());
  }

  @AssertTrue(message = "同じファイルを複数指定することはできません")
  public boolean hasUniquePendingFileUploadIds() {
    if (pendingFileUploadIds == null
      || pendingFileUploadIds.stream().anyMatch(Objects::isNull)
    ) {
      return true;
    }

    return pendingFileUploadIds
      .stream()
      .distinct()
      .count() == pendingFileUploadIds.size();
  }
}
