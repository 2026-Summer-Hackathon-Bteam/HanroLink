package com.hanrolink.product.entity;

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
@Table(name = "product_stories")
public class ProductStory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(
    name = "product_id",
    updatable = false,
    nullable = false
  )
  private Long productId;

  @Column(name = "product_story_section_template_id", nullable = false)
  private Short productStorySectionTemplateId;

  @Column(nullable = false)
  private Short position;

  @Column(nullable = false)
  private String body;

  @Column(name = "image_storage_key", nullable = false)
  private String imageStorageKey;

  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected ProductStory() {}

  public ProductStory(
    Long productId,
    Short productStorySectionTemplateId,
    Short position,
    String body,
    String imageStorageKey
  ) {
    this.productId = productId;
    this.productStorySectionTemplateId = productStorySectionTemplateId;
    this.position = position;
    this.body = body;
    this.imageStorageKey = imageStorageKey;
  }

  public void update(
    Short productStorySectionTemplateId,
    Short position,
    String body
  ) {
    this.productStorySectionTemplateId = productStorySectionTemplateId;
    this.position = position;
    this.body = body;
  }

  public void updateImageStorageKey(
    String imageStorageKey
  ) {
    this.imageStorageKey = imageStorageKey;
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

  public String getImageStorageKey() {
    return imageStorageKey;
  }
}
