package com.hanrolink.productcategory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hanrolink.productcategory.entity.ProductCategory;
import com.hanrolink.productcategory.response.component.ProductCategoryOptionResponse;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Short> {

  @Query("""
    SELECT new com.hanrolink.productcategory.response.component.ProductCategoryOptionResponse(
      productCategory.id,
      productCategory.productCategoryGroupId,
      productCategory.name
    )
    FROM ProductCategory productCategory
    ORDER BY
      productCategory.productCategoryGroupId ASC,
      productCategory.sortOrder ASC,
      productCategory.id ASC
    """)
  List<ProductCategoryOptionResponse> findAllOptions();
}
