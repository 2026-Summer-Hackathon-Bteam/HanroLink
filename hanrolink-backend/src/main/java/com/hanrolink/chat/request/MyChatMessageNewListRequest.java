package com.hanrolink.chat.request;

import java.util.Objects;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MyChatMessageNewListRequest(
  @Min(1)
  @Max(50)
  Integer limit,

  @NotNull
  @Positive
  Long afterMessageId
) {
  public MyChatMessageNewListRequest {
    limit = Objects.requireNonNullElse(limit, 50);
  }
}
