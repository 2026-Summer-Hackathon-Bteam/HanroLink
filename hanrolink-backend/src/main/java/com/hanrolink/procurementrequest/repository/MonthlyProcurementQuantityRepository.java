package com.hanrolink.procurementrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanrolink.procurementrequest.entity.MonthlyProcurementQuantity;

@Repository
public interface MonthlyProcurementQuantityRepository extends JpaRepository<MonthlyProcurementQuantity, Long> {}
