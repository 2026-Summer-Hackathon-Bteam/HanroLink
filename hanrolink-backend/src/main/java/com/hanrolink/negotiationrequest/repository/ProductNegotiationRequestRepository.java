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

import com.hanrolink.negotiationrequest.entity.ProductNegotiationRequest;
import com.hanrolink.negotiationrequest.repository.projection.BuyerSentNegotiationRequestListProjection;
import com.hanrolink.negotiationrequest.repository.projection.SupplierReceivedNegotiationRequestListProjection;

import jakarta.persistence.LockModeType;

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

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
    SELECT productNegotiationRequest
    FROM ProductNegotiationRequest productNegotiationRequest
    JOIN Product product
      ON product.id = productNegotiationRequest.productId
    JOIN BusinessUserAccount recipientAccount
      ON recipientAccount.businessId = product.supplierBusinessId
    WHERE productNegotiationRequest.publicId = :productNegotiationRequestPublicId
      AND recipientAccount.identityProviderSubject = :identityProviderSubject
      AND productNegotiationRequest.expiresAt > :currentTime
    """)
  Optional<ProductNegotiationRequest> findActiveReceivedByPublicIdAndIdentityProviderSubject(
    @Param("productNegotiationRequestPublicId")
    UUID productNegotiationRequestPublicId,
    @Param("identityProviderSubject")
    String identityProviderSubject,
    @Param("currentTime")
    Instant currentTime
  );

  @Query("""
    SELECT COUNT(productNegotiationRequest.id) > 0
    FROM ProductNegotiationRequest productNegotiationRequest
    JOIN Product product
      ON product.id = productNegotiationRequest.productId
    WHERE product.publicId = :productPublicId
      AND productNegotiationRequest.buyerAccountId = :buyerAccountId
      AND productNegotiationRequest.expiresAt > :currentTime
    """)
  boolean existsActiveByProductPublicIdAndBuyerAccountId(
    @Param("productPublicId")
    UUID productPublicId,
    @Param("buyerAccountId")
    Long buyerAccountId,
    @Param("currentTime")
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
