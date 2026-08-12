package com.hanrolink.negotiationrequest.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.negotiationrequest.entity.ProductNegotiationRequest;

@Repository
public interface ProductNegotiationRequestRepository extends JpaRepository<ProductNegotiationRequest, Long> {

  @Query("""
    SELECT COUNT(productNegotiationRequest)
    FROM ProductNegotiationRequest productNegotiationRequest
    WHERE productNegotiationRequest.buyerAccountId = :buyerAccountId
      AND productNegotiationRequest.createdAt >= :activeSince
    """)
  long countActiveByBuyerAccountId(
    @Param("buyerAccountId")
    Long buyerAccountId,
    @Param("activeSince")
    Instant activeSince
  );

  @Query("""
    SELECT COUNT(productNegotiationRequest) > 0
    FROM ProductNegotiationRequest productNegotiationRequest
    WHERE productNegotiationRequest.productId = :productId
      AND productNegotiationRequest.buyerAccountId = :buyerAccountId
      AND productNegotiationRequest.createdAt >= :activeSince
    """)
  boolean existsActiveByProductIdAndBuyerAccountId(
    @Param("productId")
    Long productId,

    @Param("buyerAccountId")
    Long buyerAccountId,

    @Param("activeSince")
    Instant activeSince
  );
}
