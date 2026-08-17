package com.hanrolink.procurementrequest.entity;

import java.time.Instant;
import java.util.UUID;

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
  private Long id;

  @Column(name = "public_id", updatable = false, nullable = false)
  private UUID publicId = UUID.randomUUID();

  @Column(name = "buyer_business_id", nullable = false)
  private Long buyerBusinessId;

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

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected ProcurementRequest() {}

  public ProcurementRequest(
    Long buyerBusinessId,
    Short productCategoryId,
    String title,
    String description,
    String requiredTradeTerms,
    Integer desiredUnitPrice,
    Short deliveryShelfLifeDays
  ) {
    this.buyerBusinessId = buyerBusinessId;
    this.productCategoryId = productCategoryId;
    this.title = title;
    this.description = description;
    this.requiredTradeTerms = requiredTradeTerms;
    this.desiredUnitPrice = desiredUnitPrice;
    this.deliveryShelfLifeDays = deliveryShelfLifeDays;
  }

  public void update(
    Short productCategoryId,
    String title,
    String description,
    String requiredTradeTerms,
    Integer desiredUnitPrice,
    Short deliveryShelfLifeDays
  ) {
    this.productCategoryId = productCategoryId;
    this.title = title;
    this.description = description;
    this.requiredTradeTerms = requiredTradeTerms;
    this.desiredUnitPrice = desiredUnitPrice;
    this.deliveryShelfLifeDays = deliveryShelfLifeDays;
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

  public Long getId() {
    return id;
  }

  public UUID getPublicId() {
    return publicId;
  }
}
