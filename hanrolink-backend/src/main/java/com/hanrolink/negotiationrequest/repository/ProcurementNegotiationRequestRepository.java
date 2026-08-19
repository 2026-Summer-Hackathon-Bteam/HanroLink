package com.hanrolink.negotiationrequest.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.negotiationrequest.entity.ProcurementNegotiationRequest;
import com.hanrolink.negotiationrequest.repository.projection.SupplierProcurementNegotiationRequestListProjection;

@Repository
public interface ProcurementNegotiationRequestRepository extends JpaRepository<ProcurementNegotiationRequest, Long> {

  @Query("""
    SELECT new com.hanrolink.negotiationrequest.repository.projection.SupplierProcurementNegotiationRequestListProjection(
      procurementNegotiationRequest.publicId,
      procurementRequest.publicId,
      procurementRequest.title,
      product.publicId,
      product.name,
      procurementNegotiationRequest.createdAt
    )
    FROM ProcurementNegotiationRequest procurementNegotiationRequest
    JOIN ProcurementRequest procurementRequest
      ON procurementRequest.id = procurementNegotiationRequest.procurementRequestId
    JOIN Product product
      ON product.id = procurementNegotiationRequest.productId
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.id = procurementNegotiationRequest.supplierAccountId
    WHERE businessUserAccount.identityProviderSubject = :identityProviderSubject
      AND procurementNegotiationRequest.createdAt >= :activeSince
    ORDER BY
      procurementNegotiationRequest.createdAt DESC,
      procurementNegotiationRequest.id DESC
    """)
  List<SupplierProcurementNegotiationRequestListProjection> findActiveListByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject,
    @Param("activeSince")
    Instant activeSince
  );

  @Query("""
    SELECT COUNT(procurementNegotiationRequest)
    FROM ProcurementNegotiationRequest procurementNegotiationRequest
    WHERE procurementNegotiationRequest.supplierAccountId = :supplierAccountId
      AND procurementNegotiationRequest.createdAt >= :activeSince
    """)
  long countActiveBySupplierAccountId(
    @Param("supplierAccountId")
    Long supplierAccountId,
    @Param("activeSince")
    Instant activeSince
  );

  @Query("""
    SELECT COUNT(procurementNegotiationRequest) > 0
    FROM ProcurementNegotiationRequest procurementNegotiationRequest
    WHERE procurementNegotiationRequest.procurementRequestId =
      :procurementRequestId
      AND procurementNegotiationRequest.supplierAccountId =
        :supplierAccountId
      AND procurementNegotiationRequest.createdAt >= :activeSince
    """)
  boolean existsActiveByProcurementRequestIdAndSupplierAccountId(
    @Param("procurementRequestId")
    Long procurementRequestId,
    @Param("supplierAccountId")
    Long supplierAccountId,
    @Param("activeSince")
    Instant activeSince
  );
}
