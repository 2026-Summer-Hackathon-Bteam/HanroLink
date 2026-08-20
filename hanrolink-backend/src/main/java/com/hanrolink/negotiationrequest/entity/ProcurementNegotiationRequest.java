package com.hanrolink.negotiationrequest.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.hanrolink.negotiationrequest.policy.NegotiationRequestPolicy;
import com.hanrolink.negotiationrequest.snapshot.ProcurementRequestSnapshot;
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
@Table(name = "procurement_negotiation_requests")
public class ProcurementNegotiationRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "public_id", updatable = false, nullable = false)
  private UUID publicId = UUID.randomUUID();

  @Column(name = "supplier_account_id", updatable = false, nullable = false)
  private Long supplierAccountId;

  @Column(name = "procurement_request_id", nullable = false, updatable = false)
  private Long procurementRequestId;

  @Column(name = "product_id", nullable = false, updatable = false)
  private Long productId;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(
    name = "procurement_request_snapshot",
    columnDefinition = "jsonb",
    updatable = false,
    nullable = false
  )
  private ProcurementRequestSnapshot procurementRequestSnapshot;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(
    name = "product_snapshot",
    columnDefinition = "jsonb",
    updatable = false,
    nullable = false
  )
  private ProductSnapshot productSnapshot;

  @Column(name = "expires_at", updatable = false, nullable = false)
  private Instant expiresAt;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected ProcurementNegotiationRequest() {}

  public ProcurementNegotiationRequest(
    Long supplierAccountId,
    Long procurementRequestId,
    Long productId,
    ProcurementRequestSnapshot procurementRequestSnapshot,
    ProductSnapshot productSnapshot
  ) {
    this.supplierAccountId = supplierAccountId;
    this.procurementRequestId = procurementRequestId;
    this.productId = productId;
    this.procurementRequestSnapshot = procurementRequestSnapshot;
    this.productSnapshot = productSnapshot;
  }

  @PrePersist
  private void onCreate() {
    Instant now = Instant.now();
    this.expiresAt = now.plus(NegotiationRequestPolicy.ACTIVE_DURATION);
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  private void onUpdate() {
    this.updatedAt = Instant.now();
  }
}
