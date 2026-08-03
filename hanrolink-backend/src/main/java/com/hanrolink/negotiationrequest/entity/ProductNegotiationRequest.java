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
@Table(name = "product_negotiation_requests")
public class ProductNegotiationRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  @Column(name = "buyer_account_id", nullable = false)
  private Long buyerAccountId;

  @Column(name = "product_id", nullable = false)
  private Long productId;

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

  protected ProductNegotiationRequest() {}

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
