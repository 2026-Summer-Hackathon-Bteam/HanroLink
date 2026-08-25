package com.hanrolink.chat.request;

import java.util.Objects;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record MyChatMessageListRequest(
  @Min(1)
  @Max(50)
  Integer limit,

  @Positive
  Long beforeMessageId,

  @Positive
  Long afterMessageId
) {
  public MyChatMessageListRequest {
    limit = Objects.requireNonNullElse(limit, 50);
  }

  @AssertTrue(message = "beforeMessageIdとafterMessageIdは同時に指定できません")
  public boolean hasSingleCursor() {
    return beforeMessageId == null
      || afterMessageId == null;
  }
}
