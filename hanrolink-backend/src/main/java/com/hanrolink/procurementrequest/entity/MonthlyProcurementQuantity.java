package com.hanrolink.procurementrequest.entity;

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
@Table(name = "monthly_procurement_quantities")
public class MonthlyProcurementQuantity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "procurement_request_id", nullable = false)
  private Long procurementRequestId;

  @Column(name = "target_month", nullable = false)
  private LocalDate targetMonth;

  @Column(name = "desired_quantity", nullable = false)
  private Integer desiredQuantity;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected MonthlyProcurementQuantity() {}

  public MonthlyProcurementQuantity(
    Long procurementRequestId,
    YearMonth targetMonth,
    Integer desiredQuantity
  ) {
    this.procurementRequestId = procurementRequestId;
    this.targetMonth = targetMonth.atDay(1);
    this.desiredQuantity = desiredQuantity;
  }

  public void updateDesiredQuantity(
    Integer desiredQuantity
  ) {
    this.desiredQuantity = desiredQuantity;
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

  public YearMonth getTargetMonth() {
    return YearMonth.from(targetMonth);
  }
}
