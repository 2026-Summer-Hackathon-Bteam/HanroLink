package com.hanrolink.procurementrequest.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.procurementrequest.entity.ProcurementRequest;
import com.hanrolink.procurementrequest.repository.projection.BuyerProcurementRequestListProjection;

@Repository
public interface ProcurementRequestRepository extends JpaRepository<ProcurementRequest, Long> {

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
}
