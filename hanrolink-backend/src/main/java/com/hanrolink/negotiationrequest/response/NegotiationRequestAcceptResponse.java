package com.hanrolink.negotiationrequest.response;

import java.util.Objects;

import com.hanrolink.negotiationrequest.response.component.NegotiationRequestChannelResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record NegotiationRequestAcceptResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  NegotiationRequestChannelResponse channel
) {
  public NegotiationRequestAcceptResponse {
    Objects.requireNonNull(
      channel,
      "NegotiationRequestAcceptResponse.channel must not be null"
    );
  }
}
