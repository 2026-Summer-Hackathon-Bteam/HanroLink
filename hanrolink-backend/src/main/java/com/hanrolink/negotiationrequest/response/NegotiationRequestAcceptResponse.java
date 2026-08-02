package com.hanrolink.negotiationrequest.response;

import com.hanrolink.negotiationrequest.response.component.NegotiationRequestChannelResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public record NegotiationRequestAcceptResponse(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  NegotiationRequestChannelResponse channel
) {}
