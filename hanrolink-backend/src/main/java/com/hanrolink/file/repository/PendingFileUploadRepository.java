package com.hanrolink.file.repository;

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
    WHERE pendingFileUpload.publicId = :publicId
      AND businessUserAccount.identityProviderSubject = :identityProviderSubject
    """)
  Optional<PendingFileUpload> findByPublicIdAndIdentityProviderSubject(
    @Param("publicId")
    UUID publicId,
    @Param("identityProviderSubject")
    String identityProviderSubject
  );
}
