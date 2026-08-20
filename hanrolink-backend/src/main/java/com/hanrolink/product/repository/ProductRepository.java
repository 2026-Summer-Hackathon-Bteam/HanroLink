package com.hanrolink.product.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.product.entity.Product;
import com.hanrolink.product.enums.StorageType;
import com.hanrolink.product.repository.projection.ProductDetailProjection;
import com.hanrolink.product.repository.projection.ProductSearchResultProjection;
import com.hanrolink.product.repository.projection.ProductSnapshotProjection;
import com.hanrolink.product.repository.projection.PublicProductListProjection;
import com.hanrolink.product.repository.projection.SupplierNegotiationRequestSelectableProductProjection;
import com.hanrolink.product.repository.projection.SupplierProductListProjection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  boolean existsByPublicIdAndHiddenAtIsNull(UUID productPublicId);

  @Query("""
    SELECT product
    FROM Product product
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.businessId = product.supplierBusinessId
    WHERE product.publicId = :productPublicId
      AND businessUserAccount.identityProviderSubject = :identityProviderSubject
    """)
  Optional<Product> findByPublicIdAndIdentityProviderSubject(
    @Param("productPublicId")
    UUID productPublicId,
    @Param("identityProviderSubject")
    String identityProviderSubject
  );

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.PublicProductListProjection(
      product.publicId,
      product.name,
      business.name,
      product.mainImageStorageKey
    )
    FROM Product product
    JOIN Business business
      ON business.id = product.supplierBusinessId
    WHERE product.hiddenAt IS NULL
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
      product.publicId,
      product.supplierBusinessId,
      product.name,
      product.hiddenAt,
      product.productCategoryId,
      productCategory.name,
      product.mainIngredientOriginPrefectureId,
      prefecture.name,
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
    JOIN Business business
      ON business.id = product.supplierBusinessId
    JOIN ProductCategory productCategory
      ON productCategory.id = product.productCategoryId
    JOIN Prefecture prefecture
      ON prefecture.id = product.mainIngredientOriginPrefectureId
    WHERE product.publicId = :productPublicId
      AND (
        product.hiddenAt IS NULL
        OR (
          :viewerBusinessId IS NOT NULL
          AND product.supplierBusinessId = :viewerBusinessId
        )
      )
    """)
  Optional<ProductDetailProjection> findDetailByPublicId(
    @Param("productPublicId")
    UUID productPublicId,
    @Param("viewerBusinessId")
    Long viewerBusinessId
  );

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.ProductSearchResultProjection(
      product.id,
      product.publicId,
      product.name,
      business.name,
      productCategory.name,
      prefecture.name,
      product.storageType,
      product.mainImageStorageKey
    )
    FROM Product product
    JOIN Business business
      ON business.id = product.supplierBusinessId
    JOIN ProductCategory productCategory
      ON productCategory.id = product.productCategoryId
    JOIN Prefecture prefecture
      ON prefecture.id = product.mainIngredientOriginPrefectureId
    WHERE product.hiddenAt IS NULL
      AND (
        :#{#mainIngredientOriginRegionIds == null || #mainIngredientOriginRegionIds.isEmpty()} = true
        OR prefecture.regionId IN :mainIngredientOriginRegionIds
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
        :#{#availableSupplyMonths.isEmpty()} = true
        OR (
          SELECT COUNT(monthlySupplyCapacity.id)
          FROM MonthlySupplyCapacity monthlySupplyCapacity
          WHERE monthlySupplyCapacity.productId = product.id
            AND monthlySupplyCapacity.targetMonth IN :availableSupplyMonths
            AND monthlySupplyCapacity.availableQuantity > 0
        ) = :#{#availableSupplyMonths.size()}
      )
    ORDER BY
      product.updatedAt DESC,
      product.id DESC
    """)
  Page<ProductSearchResultProjection> findSearchResults(
    @Param("availableSupplyMonths")
    List<LocalDate> availableSupplyMonths,
    @Param("mainIngredientOriginRegionIds")
    List<Short> mainIngredientOriginRegionIds,
    @Param("productCategoryIds")
    List<Short> productCategoryIds,
    @Param("storageTypes")
    Set<StorageType> storageTypes,
    Pageable pageable
  );

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.SupplierProductListProjection(
      product.publicId,
      product.name,
      product.mainImageStorageKey,
      product.hiddenAt,
      product.updatedAt
    )
    FROM Product product
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.businessId = product.supplierBusinessId
    WHERE businessUserAccount.identityProviderSubject = :identityProviderSubject
    ORDER BY product.updatedAt DESC
    """)
  List<SupplierProductListProjection> findManagementListByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject
  );

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.SupplierNegotiationRequestSelectableProductProjection(
      product.publicId,
      product.name,
      product.mainImageStorageKey
    )
    FROM Product product
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.businessId = product.supplierBusinessId
    WHERE businessUserAccount.identityProviderSubject = :identityProviderSubject
      AND product.hiddenAt IS NULL
    ORDER BY product.updatedAt DESC
    """)
  List<SupplierNegotiationRequestSelectableProductProjection>
    findSelectableProductsForNegotiationRequest(
      @Param("identityProviderSubject")
      String identityProviderSubject
    );

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.ProductSnapshotProjection(
      product.updatedAt,
      product.id,
      product.productCategoryId,
      productCategory.name,
      product.mainIngredientOriginPrefectureId,
      prefecture.name,
      product.name,
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
      product.salesAreaRestriction
    )
    FROM Product product
    JOIN ProductCategory productCategory
      ON productCategory.id = product.productCategoryId
    JOIN Prefecture prefecture
      ON prefecture.id = product.mainIngredientOriginPrefectureId
    WHERE product.publicId = :productPublicId
    """)
  Optional<ProductSnapshotProjection> findSnapshotByPublicId(
    @Param("productPublicId")
    UUID productPublicId
  );
}
