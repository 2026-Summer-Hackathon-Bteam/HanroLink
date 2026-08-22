package com.hanrolink.negotiationrequest.policy;

import java.time.Duration;

public final class NegotiationRequestPolicy {

  public static final Duration ACTIVE_DURATION = Duration.ofDays(7);

  private NegotiationRequestPolicy() {}
}
