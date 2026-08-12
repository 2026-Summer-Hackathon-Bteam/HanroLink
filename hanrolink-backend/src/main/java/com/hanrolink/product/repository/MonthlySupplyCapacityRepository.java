package com.hanrolink.product.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.product.entity.MonthlySupplyCapacity;
import com.hanrolink.product.repository.projection.MonthlySupplyCapacityProjection;
import com.hanrolink.product.repository.projection.ProductSearchMonthlySupplyCapacityProjection;

@Repository
public interface MonthlySupplyCapacityRepository extends JpaRepository<MonthlySupplyCapacity, Long> {

  List<MonthlySupplyCapacity> findAllByProductId(Long productId);

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.MonthlySupplyCapacityProjection(
      monthlySupplyCapacity.targetMonth,
      monthlySupplyCapacity.availableQuantity
    )
    FROM MonthlySupplyCapacity monthlySupplyCapacity
    WHERE monthlySupplyCapacity.productId = :productId
    ORDER BY monthlySupplyCapacity.targetMonth DESC
    """)
  List<MonthlySupplyCapacityProjection> findLatestListByProductId(
    @Param("productId")
    Long productId,
    Pageable pageable
  );

  @Query("""
    SELECT new com.hanrolink.product.repository.projection.ProductSearchMonthlySupplyCapacityProjection(
      monthlySupplyCapacity.productId,
      monthlySupplyCapacity.targetMonth,
      monthlySupplyCapacity.availableQuantity
    )
    FROM MonthlySupplyCapacity monthlySupplyCapacity
    WHERE monthlySupplyCapacity.productId IN :productIds
    ORDER BY
      monthlySupplyCapacity.productId ASC,
      monthlySupplyCapacity.targetMonth ASC
    """)
  List<ProductSearchMonthlySupplyCapacityProjection> findSearchListByProductIds(
    @Param("productIds")
    List<Long> productIds
  );
}
