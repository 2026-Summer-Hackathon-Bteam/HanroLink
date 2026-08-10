package com.hanrolink.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.product.entity.Product;
import com.hanrolink.product.repository.projection.SupplierProductListItem;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.SupplierProductListItem(
      product.id,
      product.name,
      product.mainImageStorageKey,
      product.hiddenAt,
      product.updatedAt
    )
    FROM Product product
    WHERE product.supplierAccountId = :supplierAccountId
      AND product.deletedAt IS NULL
    ORDER BY product.updatedAt DESC
    """)
  List<SupplierProductListItem> findListItemsBySupplierAccountId(
    @Param("supplierAccountId")
    Long supplierAccountId
  );
}
