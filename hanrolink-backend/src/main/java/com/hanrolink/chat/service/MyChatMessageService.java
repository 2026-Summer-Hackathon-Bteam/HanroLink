package com.hanrolink.chat.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.chat.entity.Message;
import com.hanrolink.chat.entity.MessageFile;
import com.hanrolink.chat.repository.ChannelRepository;
import com.hanrolink.chat.repository.MessageFileRepository;
import com.hanrolink.chat.repository.MessageRepository;
import com.hanrolink.chat.repository.projection.MyChatMessageCreateContextProjection;
import com.hanrolink.chat.request.MyChatMessageCreateRequest;
import com.hanrolink.file.entity.PendingFileUpload;
import com.hanrolink.file.enums.FileUploadUsage;
import com.hanrolink.file.repository.PendingFileUploadRepository;
import com.hanrolink.infrastructure.s3.S3UploadedFileVerifier;

@Service
public class MyChatMessageService {

  private final ChannelRepository channelRepository;

  private final MessageRepository messageRepository;

  private final MessageFileRepository messageFileRepository;

  private final PendingFileUploadRepository pendingFileUploadRepository;

  private final S3UploadedFileVerifier s3UploadedFileVerifier;

  public MyChatMessageService(
    ChannelRepository channelRepository,
    MessageRepository messageRepository,
    MessageFileRepository messageFileRepository,
    PendingFileUploadRepository pendingFileUploadRepository,
    S3UploadedFileVerifier s3UploadedFileVerifier
  ) {
    this.channelRepository = channelRepository;
    this.messageRepository = messageRepository;
    this.messageFileRepository = messageFileRepository;
    this.pendingFileUploadRepository = pendingFileUploadRepository;
    this.s3UploadedFileVerifier = s3UploadedFileVerifier;
  }

  /**
   * 指定されたチャンネルへメッセージを作成する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param channelPublicId 作成対象のチャンネル公開識別子
   * @param request メッセージの入力情報
   */
  @Transactional
  public void create(
    String identityProviderSubject,
    UUID channelPublicId,
    MyChatMessageCreateRequest request
  ) {
    // メッセージ作成対象チャンネルの当事者確認と必要な主キーの取得
    MyChatMessageCreateContextProjection messageCreateContext = channelRepository
      .findMessageCreateContextByPublicIdAndIdentityProviderSubject(
        channelPublicId,
        identityProviderSubject
      )
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 指定された添付ファイルの所有者・用途・有効期限・未使用状態とS3上の実体の検証
    List<PendingFileUpload> pendingFileUploads = List.of();
    if (request.pendingFileUploadIds() != null
      && !request.pendingFileUploadIds().isEmpty()
    ) {
      pendingFileUploads = findUsableFileUploads(
        request.pendingFileUploadIds(),
        messageCreateContext.businessUserAccountId()
      );
    }

    // メッセージの作成と保存
    Message message = new Message(
      messageCreateContext.channelId(),
      messageCreateContext.businessUserAccountId(),
      request.body()
    );
    Message savedMessage = messageRepository.save(message);

    // 添付ファイル情報の作成
    List<MessageFile> messageFiles = pendingFileUploads
      .stream()
      .map(pendingFileUpload ->
        new MessageFile(
          savedMessage.getId(),
          pendingFileUpload.getStorageKey(),
          pendingFileUpload.getMimeType(),
          pendingFileUpload.getDisplayFilename(),
          pendingFileUpload.getFileSizeBytes()
        )
      )
      .toList();
    messageFileRepository.saveAll(messageFiles);

    // 使用済みアップロード待ち情報の削除
    pendingFileUploadRepository.deleteAll(pendingFileUploads);
  }

  private List<PendingFileUpload> findUsableFileUploads(
    List<UUID> pendingFileUploadIds,
    Long businessUserAccountId
  ) {
    // 指定されたすべてのアップロード待ち情報について、所有者・用途・有効期限の確認
    List<PendingFileUpload> pendingFileUploads = pendingFileUploadRepository
      .findAllAvailableByPublicIds(
        pendingFileUploadIds,
        businessUserAccountId,
        FileUploadUsage.MESSAGE_ATTACHMENT,
        Instant.now()
      );
    if (pendingFileUploadIds.size() != pendingFileUploads.size()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // S3上のファイル内容の形式別検証
    for (PendingFileUpload pendingFileUpload : pendingFileUploads) {
      boolean isValid =
        switch (pendingFileUpload.getMimeType()) {
          case IMAGE_WEBP ->
            s3UploadedFileVerifier.isValidWebp(
              pendingFileUpload.getStorageKey(),
              pendingFileUpload.getFileSizeBytes()
            );
          case APPLICATION_PDF ->
            s3UploadedFileVerifier.isValidPdf(
              pendingFileUpload.getStorageKey(),
              pendingFileUpload.getFileSizeBytes()
            );
        };
      if (!isValid) {
        throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ファイルがアップロード条件を満たしていません"
        );
      }
    }

    return pendingFileUploads;
  }
}
