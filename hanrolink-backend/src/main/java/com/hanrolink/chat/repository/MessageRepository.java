package com.hanrolink.chat.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.chat.entity.Message;
import com.hanrolink.chat.repository.projection.ChatMessageProjection;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  @Query("""
    SELECT new com.hanrolink.chat.repository.projection.ChatMessageProjection(
      message.id,
      business.name,
      CASE
        WHEN message.businessUserAccountId = :viewerBusinessUserAccountId
        THEN true
        ELSE false
      END,
      message.body,
      message.createdAt
    )
    FROM Message message
    JOIN BusinessUserAccount senderAccount
      ON senderAccount.id = message.businessUserAccountId
    JOIN Business business
      ON business.id = senderAccount.businessId
    WHERE message.channelId = :channelId
      AND (
        :beforeMessageId IS NULL
        OR message.id < :beforeMessageId
      )
    ORDER BY message.id DESC
    """)
  List<ChatMessageProjection> findLatestOrBeforeByChannelId(
    @Param("channelId")
    Long channelId,
    @Param("viewerBusinessUserAccountId")
    Long viewerBusinessUserAccountId,
    @Param("beforeMessageId")
    Long beforeMessageId,
    Pageable pageable
  );

  @Query("""
    SELECT new com.hanrolink.chat.repository.projection.ChatMessageProjection(
      message.id,
      business.name,
      CASE
        WHEN message.businessUserAccountId = :viewerBusinessUserAccountId
        THEN true
        ELSE false
      END,
      message.body,
      message.createdAt
    )
    FROM Message message
    JOIN BusinessUserAccount senderAccount
      ON senderAccount.id = message.businessUserAccountId
    JOIN Business business
      ON business.id = senderAccount.businessId
    WHERE message.channelId = :channelId
      AND message.id > :afterMessageId
    ORDER BY message.id ASC
    """)
  List<ChatMessageProjection> findAfterByChannelId(
    @Param("channelId")
    Long channelId,
    @Param("viewerBusinessUserAccountId")
    Long viewerBusinessUserAccountId,
    @Param("afterMessageId")
    Long afterMessageId,
    Pageable pageable
  );
}
