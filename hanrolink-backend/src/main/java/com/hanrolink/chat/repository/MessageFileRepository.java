package com.hanrolink.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.chat.entity.MessageFile;
import com.hanrolink.chat.repository.projection.ChatMessageFileProjection;

@Repository
public interface MessageFileRepository extends JpaRepository<MessageFile, Long> {

  @Query("""
    SELECT new com.hanrolink.chat.repository.projection.ChatMessageFileProjection(
      messageFile.messageId,
      messageFile.displayFilename,
      messageFile.storageKey,
      messageFile.fileSizeBytes
    )
    FROM MessageFile messageFile
    WHERE messageFile.messageId IN :messageIds
    ORDER BY
      messageFile.messageId ASC,
      messageFile.id ASC
    """)
  List<ChatMessageFileProjection> findAllByMessageIds(
    @Param("messageIds")
    List<Long> messageIds
  );
}
