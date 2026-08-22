package com.hanrolink.negotiationrequest.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.negotiationrequest.entity.ProcurementNegotiationRequest;
import com.hanrolink.negotiationrequest.repository.projection.BuyerReceivedNegotiationRequestListProjection;
import com.hanrolink.negotiationrequest.repository.projection.SupplierSentNegotiationRequestListProjection;

import jakarta.persistence.LockModeType;

@Repository
public interface ProcurementNegotiationRequestRepository extends JpaRepository<ProcurementNegotiationRequest, Long> {

  long countBySupplierAccountIdAndExpiresAtAfter(
    Long supplierAccountId,
    Instant currentTime
  );

  boolean existsByProcurementRequestIdAndSupplierAccountIdAndExpiresAtAfter(
    Long procurementRequestId,
    Long supplierAccountId,
    Instant currentTime
  );

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
    SELECT procurementNegotiationRequest
    FROM ProcurementNegotiationRequest procurementNegotiationRequest
    JOIN ProcurementRequest procurementRequest
      ON procurementRequest.id = procurementNegotiationRequest.procurementRequestId
    JOIN BusinessUserAccount recipientAccount
      ON recipientAccount.businessId = procurementRequest.buyerBusinessId
    WHERE procurementNegotiationRequest.publicId =
      :procurementNegotiationRequestPublicId
      AND recipientAccount.identityProviderSubject = :identityProviderSubject
      AND procurementNegotiationRequest.expiresAt > :currentTime
    """)
  Optional<ProcurementNegotiationRequest> findActiveReceivedByPublicIdAndIdentityProviderSubject(
    @Param("procurementNegotiationRequestPublicId")
    UUID procurementNegotiationRequestPublicId,
    @Param("identityProviderSubject")
    String identityProviderSubject,
    @Param("currentTime")
    Instant currentTime
  );

  @Query("""
    SELECT COUNT(procurementNegotiationRequest) > 0
    FROM ProcurementNegotiationRequest procurementNegotiationRequest
    JOIN ProcurementRequest procurementRequest
      ON procurementRequest.id = procurementNegotiationRequest.procurementRequestId
    WHERE procurementNegotiationRequest.supplierAccountId = :supplierAccountId
      AND procurementRequest.publicId = :procurementRequestPublicId
      AND procurementNegotiationRequest.expiresAt > :currentTime
    """)
  boolean existsActiveByProcurementRequestPublicIdAndSupplierAccountId(
    @Param("procurementRequestPublicId")
    UUID procurementRequestPublicId,
    @Param("supplierAccountId")
    Long supplierAccountId,
    @Param("currentTime")
    Instant currentTime
  );

  @Query("""
    SELECT new com.hanrolink.negotiationrequest.repository.projection.BuyerReceivedNegotiationRequestListProjection(
      procurementNegotiationRequest.publicId,
      procurementRequest.publicId,
      procurementRequest.title,
      product.publicId,
      product.name,
      senderBusiness.name,
      procurementNegotiationRequest.expiresAt
    )
    FROM ProcurementNegotiationRequest procurementNegotiationRequest
    JOIN ProcurementRequest procurementRequest
      ON procurementRequest.id = procurementNegotiationRequest.procurementRequestId
    JOIN Product product
      ON product.id = procurementNegotiationRequest.productId
    JOIN BusinessUserAccount senderAccount
      ON senderAccount.id = procurementNegotiationRequest.supplierAccountId
    JOIN Business senderBusiness
      ON senderBusiness.id = senderAccount.businessId
    JOIN BusinessUserAccount recipientAccount
      ON recipientAccount.businessId = procurementRequest.buyerBusinessId
    WHERE recipientAccount.identityProviderSubject = :identityProviderSubject
      AND procurementNegotiationRequest.expiresAt > :currentTime
    ORDER BY
      procurementNegotiationRequest.createdAt DESC,
      procurementNegotiationRequest.id DESC
    """)
  List<BuyerReceivedNegotiationRequestListProjection> findActiveReceivedListByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject,
    @Param("currentTime")
    Instant currentTime
  );

  @Query("""
    SELECT new com.hanrolink.negotiationrequest.repository.projection.SupplierSentNegotiationRequestListProjection(
      procurementNegotiationRequest.publicId,
      procurementRequest.publicId,
      procurementRequest.title,
      product.publicId,
      product.name,
      procurementNegotiationRequest.expiresAt
    )
    FROM ProcurementNegotiationRequest procurementNegotiationRequest
    JOIN ProcurementRequest procurementRequest
      ON procurementRequest.id = procurementNegotiationRequest.procurementRequestId
    JOIN Product product
      ON product.id = procurementNegotiationRequest.productId
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.id = procurementNegotiationRequest.supplierAccountId
    WHERE businessUserAccount.identityProviderSubject = :identityProviderSubject
      AND procurementNegotiationRequest.expiresAt > :currentTime
    ORDER BY
      procurementNegotiationRequest.createdAt DESC,
      procurementNegotiationRequest.id DESC
    """)
  List<SupplierSentNegotiationRequestListProjection> findActiveSentListByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject,
    @Param("currentTime")
    Instant currentTime
  );
}
