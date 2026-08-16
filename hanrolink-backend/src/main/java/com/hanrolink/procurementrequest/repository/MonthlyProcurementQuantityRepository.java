package com.hanrolink.procurementrequest.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.procurementrequest.entity.MonthlyProcurementQuantity;
import com.hanrolink.procurementrequest.repository.projection.MonthlyProcurementQuantityProjection;

@Repository
public interface MonthlyProcurementQuantityRepository extends JpaRepository<MonthlyProcurementQuantity, Long> {

  List<MonthlyProcurementQuantity> findAllByProcurementRequestId(Long procurementRequestId);

  @Query("""
    SELECT new com.hanrolink.procurementrequest.repository.projection.MonthlyProcurementQuantityProjection(
      monthlyProcurementQuantity.targetMonth,
      monthlyProcurementQuantity.desiredQuantity
    )
    FROM MonthlyProcurementQuantity monthlyProcurementQuantity
    WHERE monthlyProcurementQuantity.procurementRequestId = :procurementRequestId
    ORDER BY monthlyProcurementQuantity.targetMonth DESC
    """)
  List<MonthlyProcurementQuantityProjection> findLatestListByProcurementRequestId(
    @Param("procurementRequestId")
    Long procurementRequestId,
    Pageable pageable
  );
}
