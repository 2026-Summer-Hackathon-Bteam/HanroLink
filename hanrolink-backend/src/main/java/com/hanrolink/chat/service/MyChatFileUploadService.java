package com.hanrolink.chat.service;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.chat.repository.ChannelRepository;
import com.hanrolink.chat.repository.projection.MyChatParticipantContextProjection;
import com.hanrolink.chat.request.MyChatFileUploadCreateRequest;
import com.hanrolink.chat.response.MyChatFileUploadCreateResponse;
import com.hanrolink.file.entity.PendingFileUpload;
import com.hanrolink.file.enums.FileMimeType;
import com.hanrolink.file.enums.FileUploadUsage;
import com.hanrolink.file.repository.PendingFileUploadRepository;
import com.hanrolink.infrastructure.s3.S3UploadUrlGenerator;

@Profile("s3")
@Service
public class MyChatFileUploadService {

  private final ChannelRepository channelRepository;

  private final PendingFileUploadRepository pendingFileUploadRepository;

  private final S3UploadUrlGenerator s3UploadUrlGenerator;

  public MyChatFileUploadService(
    ChannelRepository channelRepository,
    PendingFileUploadRepository pendingFileUploadRepository,
    S3UploadUrlGenerator s3UploadUrlGenerator
  ) {
    this.channelRepository = channelRepository;
    this.pendingFileUploadRepository = pendingFileUploadRepository;
    this.s3UploadUrlGenerator = s3UploadUrlGenerator;
  }

  /**
   * チャット添付ファイルをS3へ直接アップロードするための情報を発行する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param channelPublicId 対象チャンネルの公開識別子
   * @param request アップロード対象ファイルの情報
   * @return S3への直接アップロードに使用する情報
   */
  @Transactional
  public MyChatFileUploadCreateResponse create(
    String identityProviderSubject,
    UUID channelPublicId,
    MyChatFileUploadCreateRequest request
  ) {
    MyChatParticipantContextProjection channelParticipantContext =
      channelRepository
        .findParticipantContextByPublicIdAndIdentityProviderSubject(
          channelPublicId,
          identityProviderSubject
        )
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    String fileStorageKey = createStorageKey(
      request.mimeType(),
      channelPublicId
    );
    String uploadUrl = s3UploadUrlGenerator.generate(
      fileStorageKey,
      request.mimeType().getValue()
    );

    PendingFileUpload pendingFileUpload =
      new PendingFileUpload(
        channelParticipantContext.businessUserAccountId(),
        channelParticipantContext.channelId(),
        fileStorageKey,
        FileUploadUsage.MESSAGE_ATTACHMENT,
        request.displayFilename(),
        request.mimeType(),
        request.fileSizeBytes()
      );
    pendingFileUploadRepository.save(pendingFileUpload);

    return new MyChatFileUploadCreateResponse(
      uploadUrl,
      pendingFileUpload.getPublicId()
    );
  }

  private String createStorageKey(
    FileMimeType mimeType,
    UUID channelPublicId
  ) {
    return "channels/"
      + channelPublicId
      + "/files/"
      + UUID.randomUUID()
      + "."
      + mimeType.getExtension();
  }
}
