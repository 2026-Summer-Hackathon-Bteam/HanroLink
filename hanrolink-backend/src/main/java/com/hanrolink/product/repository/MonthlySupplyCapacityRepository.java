package com.hanrolink.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanrolink.product.entity.MonthlySupplyCapacity;

@Repository
public interface MonthlySupplyCapacityRepository extends JpaRepository<MonthlySupplyCapacity, Long> {}
