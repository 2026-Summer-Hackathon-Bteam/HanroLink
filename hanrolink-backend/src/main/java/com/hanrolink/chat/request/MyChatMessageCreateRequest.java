package com.hanrolink.chat.request;

import java.util.List;
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
}
