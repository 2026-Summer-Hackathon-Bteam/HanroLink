package com.hanrolink.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.product.entity.Product;
import com.hanrolink.product.repository.projection.PublicProductListItem;
import com.hanrolink.product.repository.projection.SupplierProductManagementListItem;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Optional<Product> findByIdAndSupplierAccountIdAndDeletedAtIsNull(Long id, Long supplierAccountId);

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.PublicProductListItem(
      product.name,
      business.name,
      product.mainImageStorageKey
    )
    FROM Product product
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.id = product.supplierAccountId
    JOIN Business business
      ON business.id = businessUserAccount.businessId
    WHERE product.deletedAt IS NULL
      AND product.hiddenAt IS NULL
    ORDER BY
      product.updatedAt DESC,
      product.id DESC
    """)
  List<PublicProductListItem> findPublicListItems(
    Pageable pageable
  );

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.SupplierProductManagementListItem(
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
  List<SupplierProductManagementListItem> findManagementListItemsBySupplierAccountId(
    @Param("supplierAccountId")
    Long supplierAccountId
  );
}
