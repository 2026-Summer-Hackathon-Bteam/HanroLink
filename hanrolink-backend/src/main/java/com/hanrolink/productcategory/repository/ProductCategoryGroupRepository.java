package com.hanrolink.productcategory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hanrolink.productcategory.entity.ProductCategoryGroup;
import com.hanrolink.productcategory.repository.projection.ProductCategoryGroupOptionProjection;

@Repository
public interface ProductCategoryGroupRepository extends JpaRepository<ProductCategoryGroup, Short> {

  @Query("""
    SELECT new com.hanrolink.productcategory.repository.projection.ProductCategoryGroupOptionProjection(
      productCategoryGroup.id,
      productCategoryGroup.name
    )
    FROM ProductCategoryGroup productCategoryGroup
    ORDER BY
      productCategoryGroup.sortOrder ASC,
      productCategoryGroup.id ASC
    """)
  List<ProductCategoryGroupOptionProjection> findAllOptions();
}
