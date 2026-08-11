package com.hanrolink.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.product.entity.Product;
import com.hanrolink.product.repository.projection.ProductDetailProjection;
import com.hanrolink.product.repository.projection.PublicProductListProjection;
import com.hanrolink.product.repository.projection.SupplierProductListProjection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Optional<Product> findByIdAndSupplierAccountIdAndDeletedAtIsNull(Long id, Long supplierAccountId);

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.PublicProductListProjection(
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
  List<PublicProductListProjection> findPublicList(
    Pageable pageable
  );

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.ProductDetailProjection(
      product.id,
      product.supplierAccountId,
      product.name,
      product.hiddenAt,
      product.productCategoryId,
      productCategory.name,
      product.mainIngredientRegionId,
      region.name,
      product.contentQuantity,
      product.expirationType,
      product.shelfLifeDays,
      product.storageType,
      product.desiredRetailPrice,
      product.allergyInformation,
      product.certificationInformation,
      product.caseSize,
      product.unitsPerCase,
      product.minimumOrderQuantity,
      product.shippingLeadTimeDays,
      product.salesAreaRestriction,
      product.mainImageStorageKey,
      business.supplierBusinessName,
      business.supplierBusinessAddressPrefecture,
      business.supplierBusinessAddressMunicipalityStreet,
      business.supplierBusinessAddressBuilding,
      business.supplierBusinessWebsiteUrl
    )
    FROM Product product
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.id = product.supplierAccountId
    JOIN Business business
      ON business.id = businessUserAccount.businessId
    JOIN ProductCategory productCategory
      ON productCategory.id = product.productCategoryId
    JOIN Region region
      ON region.id = product.mainIngredientRegionId
    WHERE product.id = :productId
      AND product.deletedAt IS NULL
      AND (
        product.hiddenAt IS NULL
        OR (
          :authenticatedAccountId IS NOT NULL
          AND product.supplierAccountId = :authenticatedAccountId
        )
      )
    """)
  Optional<ProductDetailProjection> findDetailById(
    @Param("productId")
    Long productId,

    @Param("authenticatedAccountId")
    Long authenticatedAccountId
  );

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.SupplierProductListProjection(
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
  List<SupplierProductListProjection> findManagementListBySupplierAccountId(
    @Param("supplierAccountId")
    Long supplierAccountId
  );
}
