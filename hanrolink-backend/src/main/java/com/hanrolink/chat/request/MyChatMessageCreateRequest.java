package com.hanrolink.chat.request;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.util.StringUtils;

import com.hanrolink.chat.policy.ChatMessagePolicy;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MyChatMessageCreateRequest(
  @Size(max = ChatMessagePolicy.MAX_BODY_LENGTH)
  String body,

  @Size(
    max = ChatMessagePolicy.MAX_ATTACHED_FILE_COUNT,
    message = "最大{max}件までです"
  )
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
