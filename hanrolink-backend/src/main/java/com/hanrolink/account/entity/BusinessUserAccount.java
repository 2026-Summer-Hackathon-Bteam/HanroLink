package com.hanrolink.account.entity;

import java.time.Instant;
import java.util.UUID;

import com.hanrolink.account.enums.BusinessUserAccountReviewStatus;
import com.hanrolink.account.enums.BusinessUserAccountRole;

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

  @Column(name = "public_id", updatable = false, nullable = false)
  private UUID publicId = UUID.randomUUID();

  @Column(
    name = "identity_provider_subject",
    updatable = false,
    nullable = false
  )
  private String identityProviderSubject;

  @Column(nullable = false)
  private BusinessUserAccountRole role;

  @Column(name = "review_status", nullable = false)
  private BusinessUserAccountReviewStatus reviewStatus;

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
