package com.hanrolink.account.entity;

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
@Table(name = "business_user_accounts")
public class BusinessUserAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "business_id", nullable = false)
  private Long businessId;

  @Column(
    name = "identity_provider_subject",
    updatable = false,
    nullable = false
  )
  private String identityProviderSubject;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name_kana", nullable = false)
  private String lastNameKana;

  @Column(name = "first_name_kana", nullable = false)
  private String firstNameKana;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String email;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected BusinessUserAccount() {}

  public BusinessUserAccount(
    Long businessId,
    String identityProviderSubject,
    String lastName,
    String firstName,
    String lastNameKana,
    String firstNameKana,
    String phoneNumber,
    String email
  ) {
    this.businessId = businessId;
    this.identityProviderSubject = identityProviderSubject;
    this.lastName = lastName;
    this.firstName = firstName;
    this.lastNameKana = lastNameKana;
    this.firstNameKana = firstNameKana;
    this.phoneNumber = phoneNumber;
    this.email = email;
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

  public Long getBusinessId() {
    return businessId;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastNameKana() {
    return lastNameKana;
  }

  public String getFirstNameKana() {
    return firstNameKana;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getEmail() {
    return email;
  }
}
