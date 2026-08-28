package com.hanrolink.chat.repository.projection;

public record MyChatParticipantContextProjection(
  Long channelId,
  Long businessUserAccountId
) {}
