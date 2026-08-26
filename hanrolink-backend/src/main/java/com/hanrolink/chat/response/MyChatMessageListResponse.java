package com.hanrolink.chat.response;

import java.util.List;
import java.util.Objects;

import com.hanrolink.chat.response.component.ChatMessageResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyChatMessageListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ChatMessageResponse> messages,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  Boolean hasReachedOldestMessage
) {
  public MyChatMessageListResponse {
    Objects.requireNonNull(
      messages,
      "MyChatMessageListResponse.messages must not be null"
    );

    Objects.requireNonNull(
      hasReachedOldestMessage,
      "MyChatMessageListResponse.hasReachedOldestMessage must not be null"
    );
  }
}
