package com.hanrolink.chat.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.chat.repository.ChannelRepository;
import com.hanrolink.chat.repository.projection.MyChatOverviewProjection;
import com.hanrolink.chat.response.MyChatOverviewResponse;

@Service
public class MyChatService {

  private final ChannelRepository channelRepository;

  public MyChatService(
    ChannelRepository channelRepository
  ) {
    this.channelRepository = channelRepository;
  }

  /**
   * 自身が当事者であるチャットの概要情報を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param channelPublicId 取得対象のチャンネル公開識別子
   * @return チャットの概要情報
   */
  @Transactional(readOnly = true)
  public MyChatOverviewResponse getOverview(
    String identityProviderSubject,
    UUID channelPublicId
  ) {
    MyChatOverviewProjection channelOverview = channelRepository
      .findOverviewByPublicIdAndIdentityProviderSubject(
        channelPublicId,
        identityProviderSubject
      )
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return new MyChatOverviewResponse(
      channelOverview.channelName(),
      channelOverview.counterpartyBusinessName()
    );
  }
}
