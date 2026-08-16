package com.hanrolink.procurementrequest.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.procurementrequest.entity.ProcurementRequest;

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
}
