package com.hanrolink.negotiationrequest.entity;

import java.time.Instant;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import tools.jackson.databind.JsonNode;

@Entity
@Table(name = "procurement_negotiation_requests")
public class ProcurementNegotiationRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "supplier_account_id", nullable = false)
  private Long supplierAccountId;

  @Column(name = "procurement_request_id")
  private Long procurementRequestId;

  @Column(name = "product_id")
  private Long productId;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(
    name = "procurement_request_snapshot",
    columnDefinition = "jsonb",
    nullable = false
  )
  private JsonNode procurementRequestSnapshot;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(
    name = "product_snapshot",
    columnDefinition = "jsonb",
    nullable = false
  )
  private JsonNode productSnapshot;

  @Column(name = "product_main_image_storage_key", nullable = false)
  private String productMainImageStorageKey;

  @Column(name = "accepted_at")
  private Instant acceptedAt;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected ProcurementNegotiationRequest() {}

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
