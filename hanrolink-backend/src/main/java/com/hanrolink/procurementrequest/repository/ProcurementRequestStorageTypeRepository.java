package com.hanrolink.procurementrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanrolink.procurementrequest.entity.ProcurementRequestStorageType;

@Repository
public interface ProcurementRequestStorageTypeRepository extends JpaRepository<ProcurementRequestStorageType, Long> {}
