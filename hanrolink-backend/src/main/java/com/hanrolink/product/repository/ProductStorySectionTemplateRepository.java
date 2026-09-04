package com.hanrolink.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hanrolink.product.entity.ProductStorySectionTemplate;
import com.hanrolink.product.repository.projection.ProductStorySectionTemplateOptionProjection;

@Repository
public interface ProductStorySectionTemplateRepository extends JpaRepository<ProductStorySectionTemplate, Short> {

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.ProductStorySectionTemplateOptionProjection(
      productStorySectionTemplate.id,
      productStorySectionTemplate.title,
      productStorySectionTemplate.imageHint,
      productStorySectionTemplate.bodyHelpText,
      productStorySectionTemplate.bodyExample
    )
    FROM ProductStorySectionTemplate productStorySectionTemplate
    ORDER BY
      productStorySectionTemplate.sortOrder ASC,
      productStorySectionTemplate.id ASC
    """)
  List<ProductStorySectionTemplateOptionProjection> findAllOptions();
}
