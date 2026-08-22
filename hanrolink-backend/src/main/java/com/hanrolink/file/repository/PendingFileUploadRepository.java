package com.hanrolink.file.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.file.entity.PendingFileUpload;

@Repository
public interface PendingFileUploadRepository extends JpaRepository<PendingFileUpload, Long> {

  @Query("""
    SELECT pendingFileUpload
    FROM PendingFileUpload pendingFileUpload
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.id = pendingFileUpload.businessUserAccountId
    WHERE pendingFileUpload.publicId = :pendingFileUploadPublicId
      AND businessUserAccount.identityProviderSubject = :identityProviderSubject
      AND pendingFileUpload.expiresAt > :currentTime
    """)
  Optional<PendingFileUpload> findByPublicIdAndIdentityProviderSubject(
    @Param("pendingFileUploadPublicId")
    UUID pendingFileUploadPublicId,
    @Param("identityProviderSubject")
    String identityProviderSubject,
    @Param("currentTime")
    Instant currentTime
  );
}
