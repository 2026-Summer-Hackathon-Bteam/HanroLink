package com.hanrolink.chat.repository.projection;

public record MyChatOverviewProjection(
  String channelName,
  String counterpartyBusinessName
) {}
