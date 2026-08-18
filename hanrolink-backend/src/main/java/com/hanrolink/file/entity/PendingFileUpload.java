package com.hanrolink.file.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.hanrolink.file.enums.FileMimeType;
import com.hanrolink.file.enums.FileUploadUsage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "pending_file_uploads")
public class PendingFileUpload {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "public_id", updatable = false, nullable = false)
  private UUID publicId = UUID.randomUUID();

  @Column(name = "business_user_account_id", nullable = false)
  private Long businessUserAccountId;

  @Column(name = "storage_key", nullable = false)
  private String storageKey;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(
    columnDefinition = "file_upload_usage",
    nullable = false
  )
  private FileUploadUsage usage;

  @Column(name = "display_filename")
  private String displayFilename;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(
    name = "mime_type",
    columnDefinition = "file_mime_type",
    nullable = false
  )
  private FileMimeType mimeType;

  @Column(name = "file_size_bytes", nullable = false)
  private Long fileSizeBytes;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected PendingFileUpload() {}

  public PendingFileUpload(
    Long businessUserAccountId,
    String storageKey,
    FileUploadUsage usage,
    String displayFilename,
    FileMimeType mimeType,
    Long fileSizeBytes
  ) {
    this.businessUserAccountId = businessUserAccountId;
    this.storageKey = storageKey;
    this.usage = usage;
    this.displayFilename = displayFilename;
    this.mimeType = mimeType;
    this.fileSizeBytes = fileSizeBytes;
  }

  @PrePersist
  private void onCreate() {
    Instant now = Instant.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  private void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public UUID getPublicId() {
    return publicId;
  }

  public String getStorageKey() {
    return storageKey;
  }

  public FileUploadUsage getUsage() {
    return usage;
  }

  public FileMimeType getMimeType() {
    return mimeType;
  }

  public Long getFileSizeBytes() {
    return fileSizeBytes;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
