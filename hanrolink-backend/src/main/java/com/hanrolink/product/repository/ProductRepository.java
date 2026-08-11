package com.hanrolink.product.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.product.entity.Product;
import com.hanrolink.product.enums.StorageType;
import com.hanrolink.product.repository.projection.ProductDetailProjection;
import com.hanrolink.product.repository.projection.ProductSearchListItemProjection;
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
      business.name,
      business.addressPrefecture,
      business.addressMunicipalityStreet,
      business.addressBuilding,
      business.websiteUrl
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
    SELECT new com.hanrolink.product.repository.projection.ProductSearchListItemProjection(
      product.id,
      product.name,
      business.name,
      productCategory.name,
      region.name,
      product.mainImageStorageKey
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
    WHERE product.deletedAt IS NULL
      AND product.hiddenAt IS NULL
      AND (
        :#{#mainIngredientRegionIds == null || #mainIngredientRegionIds.isEmpty()} = true
        OR product.mainIngredientRegionId IN :mainIngredientRegionIds
      )
      AND (
        :#{#productCategoryGroupIds == null || #productCategoryGroupIds.isEmpty()} = true
        OR productCategory.productCategoryGroupId IN :productCategoryGroupIds
      )
      AND (
        :#{#productCategoryIds == null || #productCategoryIds.isEmpty()} = true
        OR product.productCategoryId IN :productCategoryIds
      )
      AND (
        :#{#storageTypes == null || #storageTypes.isEmpty()} = true
        OR product.storageType IN :storageTypes
      )
      AND (
        :#{#availableSupplyMonths == null || #availableSupplyMonths.isEmpty()} = true
        OR EXISTS (
          SELECT monthlySupplyCapacity.id
          FROM MonthlySupplyCapacity monthlySupplyCapacity
          WHERE monthlySupplyCapacity.productId = product.id
            AND monthlySupplyCapacity.targetMonth IN :availableSupplyMonths
        )
      )
    ORDER BY
      product.updatedAt DESC,
      product.id DESC
    """)
  Page<ProductSearchListItemProjection> findSearchList(
    @Param("availableSupplyMonths")
    List<LocalDate> availableSupplyMonths,
    @Param("mainIngredientRegionIds")
    List<Short> mainIngredientRegionIds,
    @Param("productCategoryGroupIds")
    List<Short> productCategoryGroupIds,
    @Param("productCategoryIds")
    List<Short> productCategoryIds,
    @Param("storageTypes")
    Set<StorageType> storageTypes,
    Pageable pageable
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
