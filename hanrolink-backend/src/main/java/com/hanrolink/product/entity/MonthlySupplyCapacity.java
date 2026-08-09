package com.hanrolink.product.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "monthly_supply_capacities")
public class MonthlySupplyCapacity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "target_month", nullable = false)
  private LocalDate targetMonth;

  @Column(name = "available_quantity", nullable = false)
  private Integer availableQuantity;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected MonthlySupplyCapacity() {}

  public MonthlySupplyCapacity(
    Long productId,
    YearMonth targetMonth,
    Integer availableQuantity
  ) {
    this.productId = productId;
    this.targetMonth = targetMonth.atDay(1);
    this.availableQuantity = availableQuantity;
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
}
