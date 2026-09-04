package com.hanrolink.chat.service;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.chat.repository.ChannelRepository;
import com.hanrolink.infrastructure.cloudfront.CloudFrontChannelFileSignedCookieGenerator;
import com.hanrolink.infrastructure.cloudfront.CloudFrontSignedCookieValues;

@Profile("cloudfront")
@Service
public class MyChatFileAccessService {

  private final ChannelRepository channelRepository;

  private final CloudFrontChannelFileSignedCookieGenerator cloudFrontChannelFileSignedCookieGenerator;

  public MyChatFileAccessService(
    ChannelRepository channelRepository,
    CloudFrontChannelFileSignedCookieGenerator cloudFrontChannelFileSignedCookieGenerator
  ) {
    this.channelRepository = channelRepository;
    this.cloudFrontChannelFileSignedCookieGenerator = cloudFrontChannelFileSignedCookieGenerator;
  }

  /**
   * 指定されたチャンネルの添付ファイルを閲覧するための署名付きCookie情報を発行する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param channelPublicId 閲覧対象のチャンネル公開識別子
   * @return CloudFrontの署名付きCookie情報
   */
  @Transactional(readOnly = true)
  public CloudFrontSignedCookieValues create(
    String identityProviderSubject,
    UUID channelPublicId
  ) {
    boolean isParticipant = channelRepository
      .existsByPublicIdAndIdentityProviderSubject(
        channelPublicId,
        identityProviderSubject
      );
    if (!isParticipant) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    return cloudFrontChannelFileSignedCookieGenerator.generate(channelPublicId);
  }
}
