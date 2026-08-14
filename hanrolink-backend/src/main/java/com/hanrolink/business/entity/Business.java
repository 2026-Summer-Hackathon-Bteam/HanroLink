package com.hanrolink.business.entity;

import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.hanrolink.business.enums.BusinessReviewStatus;
import com.hanrolink.business.enums.BusinessRole;

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
@Table(name = "businesses")
public class Business {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "public_id", updatable = false, nullable = false)
  private UUID publicId = UUID.randomUUID();

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(
    columnDefinition = "business_role",
    updatable = false,
    nullable = false
  )
  private BusinessRole role;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(
    name = "review_status",
    columnDefinition = "business_review_status",
    nullable = false
  )
  private BusinessReviewStatus reviewStatus =
    BusinessReviewStatus.PENDING;

  @Column(nullable = false)
  private String name;

  @Column(name = "name_kana", nullable = false)
  private String nameKana;

  @Column(name = "website_url")
  private String websiteUrl;

  @JdbcTypeCode(Types.CHAR)
  @Column(
    name = "address_postal_code",
    columnDefinition = "CHAR(7)",
    length = 7,
    nullable = false
  )
  private String addressPostalCode;

  @Column(name = "address_prefecture", nullable = false)
  private String addressPrefecture;

  @Column(name = "address_municipality_street", nullable = false)
  private String addressMunicipalityStreet;

  @Column(name = "address_building")
  private String addressBuilding;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected Business() {}

  public Business(
    BusinessRole role,
    String name,
    String nameKana,
    String websiteUrl,
    String addressPostalCode,
    String addressPrefecture,
    String addressMunicipalityStreet,
    String addressBuilding,
    String phoneNumber
  ) {
    this.role = role;
    this.name = name;
    this.nameKana = nameKana;
    this.websiteUrl = websiteUrl;
    this.addressPostalCode = addressPostalCode;
    this.addressPrefecture = addressPrefecture;
    this.addressMunicipalityStreet = addressMunicipalityStreet;
    this.addressBuilding = addressBuilding;
    this.phoneNumber = phoneNumber;
  }

  public void approve() {
    this.reviewStatus = BusinessReviewStatus.APPROVED;
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

  public BusinessRole getRole() {
    return role;
  }

  public BusinessReviewStatus getReviewStatus() {
    return reviewStatus;
  }

  public String getName() {
    return name;
  }

  public String getNameKana() {
    return nameKana;
  }

  public String getWebsiteUrl() {
    return websiteUrl;
  }

  public String getAddressPostalCode() {
    return addressPostalCode;
  }

  public String getAddressPrefecture() {
    return addressPrefecture;
  }

  public String getAddressMunicipalityStreet() {
    return addressMunicipalityStreet;
  }

  public String getAddressBuilding() {
    return addressBuilding;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
