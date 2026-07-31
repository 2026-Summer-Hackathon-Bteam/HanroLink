package com.hanrolink.product.entity;

import java.time.Instant;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.hanrolink.product.enums.ProductExpirationType;
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
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "supplier_account_id", nullable = false)
  private Long supplierAccountId;

  @Column(name = "product_category_id", nullable = false)
  private Short productCategoryId;

  @Column(name = "main_ingredient_region_id", nullable = false)
  private Short mainIngredientRegionId;

  @Column(nullable = false)
  private String name;

  @Column(name = "main_image_storage_key", nullable = false)
  private String mainImageStorageKey;

  @Column(name = "content_quantity", nullable = false)
  private String contentQuantity;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(
    name = "expiration_type",
    columnDefinition = "product_expiration_type",
    nullable = false
  )
  private ProductExpirationType expirationType;

  @Column(name = "shelf_life_days")
  private Short shelfLifeDays;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(
    name = "storage_type",
    columnDefinition = "storage_type",
    nullable = false
  )
  private StorageType storageType;

  @Column(name = "desired_retail_price", nullable = false)
  private Integer desiredRetailPrice;

  @Column(name = "allergy_information")
  private String allergyInformation;

  @Column(name = "certification_information")
  private String certificationInformation;

  @Column(name = "case_size")
  private String caseSize;

  @Column(name = "units_per_case")
  private Integer unitsPerCase;

  @Column(name = "minimum_order_quantity")
  private Integer minimumOrderQuantity;

  @Column(name = "shipping_lead_time_days")
  private Short shippingLeadTimeDays;

  @Column(name = "sales_area_restriction")
  private String salesAreaRestriction;

  @Column(name = "hidden_at")
  private Instant hiddenAt;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected Product() {}

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
