package com.hanrolink.negotiationrequest.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.negotiationrequest.entity.ProductNegotiationRequest;
import com.hanrolink.negotiationrequest.repository.projection.BuyerSentNegotiationRequestListProjection;
import com.hanrolink.negotiationrequest.repository.projection.SupplierReceivedNegotiationRequestListProjection;

@Repository
public interface ProductNegotiationRequestRepository extends JpaRepository<ProductNegotiationRequest, Long> {

  long countByBuyerAccountIdAndExpiresAtAfter(
    Long buyerAccountId,
    Instant currentTime
  );

  boolean existsByProductIdAndBuyerAccountIdAndExpiresAtAfter(
    Long productId,
    Long buyerAccountId,
    Instant currentTime
  );

  @Query("""
    SELECT new com.hanrolink.negotiationrequest.repository.projection.SupplierReceivedNegotiationRequestListProjection(
      productNegotiationRequest.publicId,
      product.publicId,
      product.name,
      senderBusiness.publicId,
      senderBusiness.name,
      productNegotiationRequest.expiresAt
    )
    FROM ProductNegotiationRequest productNegotiationRequest
    JOIN Product product
      ON product.id = productNegotiationRequest.productId
    JOIN BusinessUserAccount senderAccount
      ON senderAccount.id = productNegotiationRequest.buyerAccountId
    JOIN Business senderBusiness
      ON senderBusiness.id = senderAccount.businessId
    JOIN BusinessUserAccount recipientAccount
      ON recipientAccount.businessId = product.supplierBusinessId
    WHERE recipientAccount.identityProviderSubject = :identityProviderSubject
      AND productNegotiationRequest.expiresAt > :currentTime
    ORDER BY
      productNegotiationRequest.createdAt DESC,
      productNegotiationRequest.id DESC
    """)
  List<SupplierReceivedNegotiationRequestListProjection> findActiveReceivedListByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject,
    @Param("currentTime")
    Instant currentTime
  );

  @Query("""
    SELECT new com.hanrolink.negotiationrequest.repository.projection.BuyerSentNegotiationRequestListProjection(
      productNegotiationRequest.publicId,
      product.publicId,
      product.name,
      productNegotiationRequest.expiresAt
    )
    FROM ProductNegotiationRequest productNegotiationRequest
    JOIN Product product
      ON product.id = productNegotiationRequest.productId
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.id = productNegotiationRequest.buyerAccountId
    WHERE businessUserAccount.identityProviderSubject = :identityProviderSubject
      AND productNegotiationRequest.expiresAt > :currentTime
    ORDER BY
      productNegotiationRequest.createdAt DESC,
      productNegotiationRequest.id DESC
    """)
  List<BuyerSentNegotiationRequestListProjection> findActiveSentListByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject,
    @Param("currentTime")
    Instant currentTime
  );
}
