package com.hanrolink.negotiationrequest.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.hanrolink.negotiationrequest.snapshot.ProductSnapshot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_negotiation_requests")
public class ProductNegotiationRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "public_id", updatable = false, nullable = false)
  private UUID publicId = UUID.randomUUID();

  @Column(name = "buyer_account_id", updatable = false, nullable = false)
  private Long buyerAccountId;

  @Column(name = "supplier_business_id", updatable = false, nullable = false)
  private Long supplierBusinessId;

  @Column(name = "product_id", updatable = false)
  private Long productId;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(
    name = "product_snapshot",
    columnDefinition = "jsonb",
    updatable = false,
    nullable = false
  )
  private ProductSnapshot productSnapshot;

  @Column(name = "product_main_image_storage_key", updatable = false, nullable = false)
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
