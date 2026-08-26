package com.hanrolink.chat.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.chat.entity.Message;
import com.hanrolink.chat.entity.MessageFile;
import com.hanrolink.chat.repository.ChannelRepository;
import com.hanrolink.chat.repository.MessageFileRepository;
import com.hanrolink.chat.repository.MessageRepository;
import com.hanrolink.chat.repository.projection.ChatMessageFileProjection;
import com.hanrolink.chat.repository.projection.ChatMessageProjection;
import com.hanrolink.chat.repository.projection.MyChatParticipantContextProjection;
import com.hanrolink.chat.request.MyChatMessageCreateRequest;
import com.hanrolink.chat.request.MyChatMessageListRequest;
import com.hanrolink.chat.response.MyChatMessageListResponse;
import com.hanrolink.chat.response.component.ChatMessageFileResponse;
import com.hanrolink.chat.response.component.ChatMessageResponse;
import com.hanrolink.file.entity.PendingFileUpload;
import com.hanrolink.file.enums.FileUploadUsage;
import com.hanrolink.file.repository.PendingFileUploadRepository;
import com.hanrolink.infrastructure.cloudfront.CloudFrontResourceUrlGenerator;
import com.hanrolink.infrastructure.s3.S3UploadedFileVerifier;

@Profile("s3 & cloudfront")
@Service
public class MyChatMessageService {

  private final ChannelRepository channelRepository;

  private final MessageRepository messageRepository;

  private final MessageFileRepository messageFileRepository;

  private final PendingFileUploadRepository pendingFileUploadRepository;

  private final S3UploadedFileVerifier s3UploadedFileVerifier;

  private final CloudFrontResourceUrlGenerator cloudFrontResourceUrlGenerator;

  public MyChatMessageService(
    ChannelRepository channelRepository,
    MessageRepository messageRepository,
    MessageFileRepository messageFileRepository,
    PendingFileUploadRepository pendingFileUploadRepository,
    S3UploadedFileVerifier s3UploadedFileVerifier,
    CloudFrontResourceUrlGenerator cloudFrontResourceUrlGenerator
  ) {
    this.channelRepository = channelRepository;
    this.messageRepository = messageRepository;
    this.messageFileRepository = messageFileRepository;
    this.pendingFileUploadRepository = pendingFileUploadRepository;
    this.s3UploadedFileVerifier = s3UploadedFileVerifier;
    this.cloudFrontResourceUrlGenerator = cloudFrontResourceUrlGenerator;
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
    MyChatParticipantContextProjection channelParticipantContext = channelRepository
      .findParticipantContextByPublicIdAndIdentityProviderSubject(
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
        channelParticipantContext.channelId(),
        channelParticipantContext.businessUserAccountId()
      );
    }

    // メッセージの作成と保存
    Message message = new Message(
      channelParticipantContext.channelId(),
      channelParticipantContext.businessUserAccountId(),
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

  /**
   * 指定されたチャンネルのメッセージ一覧を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param channelPublicId 取得対象のチャンネル公開識別子
   * @param request メッセージ一覧の取得条件
   * @return 添付ファイル情報を含むメッセージ一覧
   */
  @Transactional(readOnly = true)
  public MyChatMessageListResponse list(
    String identityProviderSubject,
    UUID channelPublicId,
    MyChatMessageListRequest request
  ) {
    // 対象チャンネルの当事者確認
    MyChatParticipantContextProjection channelParticipantContext = channelRepository
      .findParticipantContextByPublicIdAndIdentityProviderSubject(
        channelPublicId,
        identityProviderSubject
      )
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 指定されたメッセージより後に作成されたメッセージの取得
    if (request.afterMessageId() != null) {
      Pageable pageable = PageRequest.of(0, request.limit());
      List<ChatMessageProjection> messageProjections =
        messageRepository.findAfterByChannelId(
          channelParticipantContext.channelId(),
          channelParticipantContext.businessUserAccountId(),
          request.afterMessageId(),
          pageable
        );

      return new MyChatMessageListResponse(
        toMessageResponses(messageProjections),
        false
      );
    }

    Pageable pageableWithLookahead = PageRequest.of(0, request.limit() + 1);
    List<ChatMessageProjection> messageProjectionsWithLookahead =
      messageRepository.findLatestOrBeforeByChannelId(
        channelParticipantContext.channelId(),
        channelParticipantContext.businessUserAccountId(),
        request.beforeMessageId(),
        pageableWithLookahead
      );

    // 最古のメッセージへの到達判定とレスポンス対象件数への制限
    boolean hasReachedOldestMessage =
      messageProjectionsWithLookahead.size() <= request.limit();
    List<ChatMessageProjection> messageProjections =
      messageProjectionsWithLookahead
        .stream()
        .limit(request.limit())
        .toList();

    return new MyChatMessageListResponse(
      toMessageResponses(messageProjections),
      hasReachedOldestMessage
    );
  }

  private List<PendingFileUpload> findUsableFileUploads(
    List<UUID> pendingFileUploadIds,
    Long channelId,
    Long businessUserAccountId
  ) {
    // 指定されたすべてのアップロード待ち情報について、所有者・対象チャンネル・用途・有効期限の確認
    List<PendingFileUpload> pendingFileUploads = pendingFileUploadRepository
      .findAllAvailableByPublicIds(
        pendingFileUploadIds,
        businessUserAccountId,
        channelId,
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

  private List<ChatMessageResponse> toMessageResponses(
    List<ChatMessageProjection> messageProjections
  ) {
    // 取得したメッセージに紐づく添付ファイル情報の一括取得
    List<Long> messageIds = messageProjections
      .stream()
      .map(message -> message.id())
      .toList();
    List<ChatMessageFileProjection> messageFiles = List.of();
    if (!messageIds.isEmpty()) {
      messageFiles = messageFileRepository
        .findAllByMessageIds(messageIds);
    }
    Map<Long, List<ChatMessageFileResponse>> messageFilesByMessageId =
      messageFiles
        .stream()
        .collect(
          Collectors.groupingBy(
            messageFile -> messageFile.messageId(),
            Collectors.mapping(
              messageFile ->
                new ChatMessageFileResponse(
                  messageFile.displayFilename(),
                  cloudFrontResourceUrlGenerator.generate(
                    messageFile.storageKey()
                  ),
                  messageFile.fileSizeBytes()
                ),
              Collectors.toList()
            )
          )
        );

    // 添付ファイル情報を含むメッセージ一覧の生成
    return messageProjections
      .stream()
      .map(message ->
        new ChatMessageResponse(
          message.id(),
          message.senderBusinessName(),
          message.isMine(),
          message.body(),
          message.createdAt(),
          messageFilesByMessageId
            .getOrDefault(
              message.id(),
              List.of()
            )
        )
      )
      .toList();
  }
}
