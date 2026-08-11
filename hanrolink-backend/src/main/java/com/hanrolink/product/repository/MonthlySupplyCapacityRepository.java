package com.hanrolink.product.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.product.entity.MonthlySupplyCapacity;
import com.hanrolink.product.response.component.MonthlySupplyCapacityResponse;

@Repository
public interface MonthlySupplyCapacityRepository extends JpaRepository<MonthlySupplyCapacity, Long> {

  @Query("""
    SELECT new com.hanrolink.product.response.component.MonthlySupplyCapacityResponse(
      monthlySupplyCapacity.targetMonth,
      monthlySupplyCapacity.availableQuantity
    )
    FROM MonthlySupplyCapacity monthlySupplyCapacity
    WHERE monthlySupplyCapacity.productId = :productId
    ORDER BY monthlySupplyCapacity.targetMonth ASC
    """)
  List<MonthlySupplyCapacityResponse> findListByProductId(
    @Param("productId")
    Long productId,
    Pageable pageable
  );
}
