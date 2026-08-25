package com.hanrolink.chat.repository.projection;

import java.time.Instant;
import java.util.UUID;

public record MyChatListProjection(
  UUID publicId,
  String name,
  Instant lastActivityAt
) {}
