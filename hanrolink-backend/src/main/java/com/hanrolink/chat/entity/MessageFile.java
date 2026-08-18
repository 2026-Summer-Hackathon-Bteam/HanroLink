package com.hanrolink.chat.entity;

import java.time.Instant;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.hanrolink.file.enums.FileMimeType;

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
@Table(name = "message_files")
public class MessageFile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "message_id", nullable = false)
  private Long messageId;

  @Column(name = "storage_key", nullable = false)
  private String storageKey;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(
    name = "mime_type",
    columnDefinition = "file_mime_type",
    nullable = false
  )
  private FileMimeType mimeType;

  @Column(name = "display_filename", nullable = false)
  private String displayFilename;

  @Column(name = "file_size_bytes", nullable = false)
  private Long fileSizeBytes;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected MessageFile() {}

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
}
