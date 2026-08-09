package com.hanrolink.business.entity;

import java.sql.Types;
import java.time.Instant;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    String name,
    String nameKana,
    String websiteUrl,
    String addressPostalCode,
    String addressPrefecture,
    String addressMunicipalityStreet,
    String addressBuilding,
    String phoneNumber
  ) {
    this.name = name;
    this.nameKana = nameKana;
    this.websiteUrl = websiteUrl;
    this.addressPostalCode = addressPostalCode;
    this.addressPrefecture = addressPrefecture;
    this.addressMunicipalityStreet = addressMunicipalityStreet;
    this.addressBuilding = addressBuilding;
    this.phoneNumber = phoneNumber;
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

  public String getName() {
    return name;
  }

  public String getWebsiteUrl() {
    return websiteUrl;
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
}
