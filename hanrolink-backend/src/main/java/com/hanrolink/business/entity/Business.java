package com.hanrolink.business.entity;

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
@Table(name = "businesses")
public class Business {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  @Column(nullable = false)
  private String name;

  @Column(name = "name_kana", nullable = false)
  private String nameKana;

  @Column(name = "website_url")
  private String websiteUrl;

  @Column(name = "address_postal_code", nullable = false)
  private String addressPostalCode;

  @Column(name = "address_prefecture", nullable = false)
  private String addressPrefecture;

  @Column(name = "address_municipality_street", nullable = false)
  private String addressMunicipalityStreet;

  @Column(name = "address_building", nullable = false)
  private String addressBuilding;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected Business() {}

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
