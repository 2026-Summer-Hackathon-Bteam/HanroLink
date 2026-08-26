package com.hanrolink.chat.repository.projection;

import java.time.Instant;

public record ChatMessageProjection(
  Long id,
  String senderBusinessName,
  Boolean isMine,
  String body,
  Instant createdAt
) {}
