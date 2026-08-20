package com.hanrolink.chat.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.hanrolink.chat.enums.NegotiationTargetType;
import com.hanrolink.negotiationrequest.snapshot.ProcurementRequestSnapshot;
import com.hanrolink.negotiationrequest.snapshot.ProductSnapshot;

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
@Table(name = "channels")
public class Channel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "public_id", updatable = false, nullable = false)
  private UUID publicId = UUID.randomUUID();

  @Column(name = "supplier_account_id", updatable = false, nullable = false)
  private Long supplierAccountId;

  @Column(name = "buyer_account_id", updatable = false, nullable = false)
  private Long buyerAccountId;

  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(
    name = "negotiation_target_type",
    columnDefinition = "negotiation_target_type",
    updatable = false,
    nullable = false
  )
  private NegotiationTargetType negotiationTargetType;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(
    name = "requested_product_snapshot",
    columnDefinition = "jsonb",
    updatable = false,
    nullable = false
  )
  private ProductSnapshot requestedProductSnapshot;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(
    name = "accepted_product_snapshot",
    columnDefinition = "jsonb",
    updatable = false,
    nullable = false
  )
  private ProductSnapshot acceptedProductSnapshot;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(
    name = "requested_procurement_request_snapshot",
    columnDefinition = "jsonb",
    updatable = false
  )
  private ProcurementRequestSnapshot requestedProcurementRequestSnapshot;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(
    name = "accepted_procurement_request_snapshot",
    columnDefinition = "jsonb",
    updatable = false
  )
  private ProcurementRequestSnapshot acceptedProcurementRequestSnapshot;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected Channel() {}

  public Channel(
    Long supplierAccountId,
    Long buyerAccountId,
    String name,
    NegotiationTargetType negotiationTargetType,
    ProductSnapshot requestedProductSnapshot,
    ProductSnapshot acceptedProductSnapshot,
    ProcurementRequestSnapshot requestedProcurementRequestSnapshot,
    ProcurementRequestSnapshot acceptedProcurementRequestSnapshot
  ) {
    this.supplierAccountId = supplierAccountId;
    this.buyerAccountId = buyerAccountId;
    this.name = name;
    this.negotiationTargetType = negotiationTargetType;
    this.requestedProductSnapshot = requestedProductSnapshot;
    this.acceptedProductSnapshot = acceptedProductSnapshot;
    this.requestedProcurementRequestSnapshot = requestedProcurementRequestSnapshot;
    this.acceptedProcurementRequestSnapshot = acceptedProcurementRequestSnapshot;
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

  public Long getBuyerAccountId() {
    return buyerAccountId;
  }

  public String getName() {
    return name;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
