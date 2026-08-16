package com.hanrolink.negotiationrequest.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.negotiationrequest.entity.ProcurementNegotiationRequest;

@Repository
public interface ProcurementNegotiationRequestRepository extends JpaRepository<ProcurementNegotiationRequest, Long> {

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
