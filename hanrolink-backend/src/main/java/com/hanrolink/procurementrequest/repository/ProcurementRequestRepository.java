package com.hanrolink.procurementrequest.repository;

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

import com.hanrolink.procurementrequest.entity.ProcurementRequest;
import com.hanrolink.procurementrequest.repository.projection.BuyerProcurementRequestListProjection;
import com.hanrolink.procurementrequest.repository.projection.ProcurementRequestDetailProjection;
import com.hanrolink.procurementrequest.repository.projection.ProcurementRequestSearchResultProjection;
import com.hanrolink.procurementrequest.repository.projection.ProcurementRequestSnapshotProjection;
import com.hanrolink.product.enums.StorageType;

@Repository
public interface ProcurementRequestRepository extends JpaRepository<ProcurementRequest, Long> {

  boolean existsByPublicId(UUID procurementRequestPublicId);

  @Query("""
    SELECT procurementRequest
    FROM ProcurementRequest procurementRequest
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.businessId = procurementRequest.buyerBusinessId
    WHERE procurementRequest.publicId = :procurementRequestPublicId
      AND businessUserAccount.identityProviderSubject = :identityProviderSubject
    """)
  Optional<ProcurementRequest> findByPublicIdAndIdentityProviderSubject(
    @Param("procurementRequestPublicId")
    UUID procurementRequestPublicId,
    @Param("identityProviderSubject")
    String identityProviderSubject
  );

  @Query("""
    SELECT new com.hanrolink.procurementrequest.repository.projection.ProcurementRequestDetailProjection(
      procurementRequest.id,
      procurementRequest.publicId,
      procurementRequest.buyerBusinessId,
      business.publicId,
      business.name,
      procurementRequest.productCategoryId,
      productCategory.name,
      procurementRequest.title,
      procurementRequest.description,
      procurementRequest.requiredTradeTerms,
      procurementRequest.desiredUnitPrice,
      procurementRequest.deliveryShelfLifeDays
    )
    FROM ProcurementRequest procurementRequest
    JOIN Business business
      ON business.id = procurementRequest.buyerBusinessId
    JOIN ProductCategory productCategory
      ON productCategory.id = procurementRequest.productCategoryId
    WHERE procurementRequest.publicId = :procurementRequestPublicId
    """)
  Optional<ProcurementRequestDetailProjection> findDetailByPublicId(
    @Param("procurementRequestPublicId")
    UUID procurementRequestPublicId
  );

  @Query("""
    SELECT new com.hanrolink.procurementrequest.repository.projection.ProcurementRequestSearchResultProjection(
      procurementRequest.id,
      procurementRequest.publicId,
      procurementRequest.title,
      procurementRequest.description,
      productCategory.name,
      business.publicId,
      business.name
    )
    FROM ProcurementRequest procurementRequest
    JOIN ProductCategory productCategory
      ON productCategory.id = procurementRequest.productCategoryId
    JOIN Business business
      ON business.id = procurementRequest.buyerBusinessId
    WHERE (
      :#{#productCategoryIds == null || #productCategoryIds.isEmpty()} = true
      OR procurementRequest.productCategoryId IN :productCategoryIds
    )
      AND (
        :#{#storageTypes == null || #storageTypes.isEmpty()} = true
        OR EXISTS (
          SELECT procurementRequestStorageType.id
          FROM ProcurementRequestStorageType procurementRequestStorageType
          WHERE procurementRequestStorageType.procurementRequestId =
            procurementRequest.id
            AND procurementRequestStorageType.storageType IN :storageTypes
        )
      )
      AND (
        :#{#desiredProcurementMonths.isEmpty()} = true
        OR (
          SELECT COUNT(monthlyProcurementQuantity.id)
          FROM MonthlyProcurementQuantity monthlyProcurementQuantity
          WHERE monthlyProcurementQuantity.procurementRequestId = procurementRequest.id
            AND monthlyProcurementQuantity.targetMonth IN :desiredProcurementMonths
            AND monthlyProcurementQuantity.desiredQuantity > 0
        ) = :#{#desiredProcurementMonths.size()}
      )
      AND (
        :#{#keyword == null || #keyword.isBlank()} = true
        OR LOWER(procurementRequest.title)
          LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(procurementRequest.description)
          LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    ORDER BY
      procurementRequest.updatedAt DESC,
      procurementRequest.id DESC
    """)
  Page<ProcurementRequestSearchResultProjection> findSearchResults(
    @Param("desiredProcurementMonths")
    List<LocalDate> desiredProcurementMonths,
    @Param("productCategoryIds")
    List<Short> productCategoryIds,
    @Param("storageTypes")
    Set<StorageType> storageTypes,
    @Param("keyword")
    String keyword,
    Pageable pageable
  );

  @Query("""
    SELECT new com.hanrolink.procurementrequest.repository.projection.BuyerProcurementRequestListProjection(
      procurementRequest.publicId,
      procurementRequest.title,
      procurementRequest.updatedAt
    )
    FROM ProcurementRequest procurementRequest
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.businessId = procurementRequest.buyerBusinessId
    WHERE businessUserAccount.identityProviderSubject = :identityProviderSubject
    ORDER BY procurementRequest.updatedAt DESC
    """)
  List<BuyerProcurementRequestListProjection> findManagementListByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject
  );

  @Query("""
    SELECT new com.hanrolink.procurementrequest.repository.projection.ProcurementRequestSnapshotProjection(
      procurementRequest.updatedAt,
      procurementRequest.id,
      procurementRequest.productCategoryId,
      productCategory.name,
      procurementRequest.title,
      procurementRequest.description,
      procurementRequest.requiredTradeTerms,
      procurementRequest.desiredUnitPrice,
      procurementRequest.deliveryShelfLifeDays
    )
    FROM ProcurementRequest procurementRequest
    JOIN ProductCategory productCategory
      ON productCategory.id = procurementRequest.productCategoryId
    WHERE procurementRequest.publicId = :procurementRequestPublicId
    """)
  Optional<ProcurementRequestSnapshotProjection> findSnapshotByPublicId(
    @Param("procurementRequestPublicId")
    UUID procurementRequestPublicId
  );
}
