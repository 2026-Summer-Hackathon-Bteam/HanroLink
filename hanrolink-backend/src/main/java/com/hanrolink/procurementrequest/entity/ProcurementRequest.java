package com.hanrolink.procurementrequest.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "procurement_requests")
public class ProcurementRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  @Column(name = "buyer_account_id", nullable = false)
  private Long buyerAccountId;

  @Column(name = "product_category_id", nullable = false)
  private Short productCategoryId;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(name = "required_trade_terms")
  private String requiredTradeTerms;

  @Column(name = "desired_unit_price")
  private Integer desiredUnitPrice;

  @Column(name = "delivery_shelf_life_days")
  private Short deliveryShelfLifeDays;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected ProcurementRequest() {}

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
