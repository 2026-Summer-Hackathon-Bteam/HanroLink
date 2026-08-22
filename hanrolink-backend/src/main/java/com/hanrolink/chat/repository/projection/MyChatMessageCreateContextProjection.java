package com.hanrolink.chat.repository.projection;

public record MyChatMessageCreateContextProjection(
  Long channelId,
  Long businessUserAccountId
) {}
