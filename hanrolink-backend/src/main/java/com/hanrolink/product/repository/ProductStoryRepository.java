package com.hanrolink.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.product.entity.ProductStory;
import com.hanrolink.product.repository.projection.ProductStoryProjection;
import com.hanrolink.product.repository.projection.ProductStorySnapshotProjection;

@Repository
public interface ProductStoryRepository extends JpaRepository<ProductStory, Long> {

  List<ProductStory> findAllByProductId(Long productId);

  @Query("""
    SELECT productStory.imageStorageKey
    FROM ProductStory productStory
    WHERE productStory.productId = :productId
    ORDER BY productStory.position ASC
    """)
  List<String> findImageStorageKeysByProductId(
    @Param("productId")
    Long productId
  );

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.ProductStoryProjection(
      productStory.id,
      productStory.productStorySectionTemplateId,
      productStory.position,
      productStorySectionTemplate.title,
      productStory.body,
      productStory.imageStorageKey
    )
    FROM ProductStory productStory
    JOIN ProductStorySectionTemplate productStorySectionTemplate
      ON productStorySectionTemplate.id = productStory.productStorySectionTemplateId
    WHERE productStory.productId = :productId
    ORDER BY productStory.position ASC
    """)
  List<ProductStoryProjection> findListByProductId(
    @Param("productId")
    Long productId
  );

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.ProductStorySnapshotProjection(
      productStory.productStorySectionTemplateId,
      productStorySectionTemplate.title,
      productStory.body
    )
    FROM ProductStory productStory
    JOIN ProductStorySectionTemplate productStorySectionTemplate
      ON productStorySectionTemplate.id = productStory.productStorySectionTemplateId
    WHERE productStory.productId = :productId
    ORDER BY productStory.position ASC
    """)
  List<ProductStorySnapshotProjection> findSnapshotByProductId(
    @Param("productId")
    Long productId
  );
}
