package com.hanrolink.chat.response;

import java.util.List;
import java.util.Objects;

import com.hanrolink.chat.response.component.ChatMessageResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyChatMessageNewListResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<ChatMessageResponse> messages
) {
  public MyChatMessageNewListResponse {
    Objects.requireNonNull(
      messages,
      "MyChatMessageNewListResponse.messages must not be null"
    );
  }
}
