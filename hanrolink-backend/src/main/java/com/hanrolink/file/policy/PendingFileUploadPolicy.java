package com.hanrolink.file.policy;

import java.time.Duration;

public final class PendingFileUploadPolicy {

  public static final Duration VALID_DURATION = Duration.ofMinutes(30);

  private PendingFileUploadPolicy() {}
}
