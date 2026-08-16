package com.hanrolink.procurementrequest.entity;

import java.time.Instant;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.hanrolink.product.enums.StorageType;

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
@Table(name = "procurement_request_storage_types")
public class ProcurementRequestStorageType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "procurement_request_id", nullable = false)
  private Long procurementRequestId;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(
    name = "storage_type",
    columnDefinition = "storage_type",
    nullable = false
  )
  private StorageType storageType;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected ProcurementRequestStorageType() {}

  public ProcurementRequestStorageType(
    Long procurementRequestId,
    StorageType storageType
  ) {
    this.procurementRequestId = procurementRequestId;
    this.storageType = storageType;
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

  public StorageType getStorageType() {
    return storageType;
  }
}
