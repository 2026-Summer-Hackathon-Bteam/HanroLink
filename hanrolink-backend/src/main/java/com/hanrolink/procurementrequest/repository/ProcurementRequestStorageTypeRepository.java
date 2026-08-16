package com.hanrolink.procurementrequest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanrolink.procurementrequest.entity.ProcurementRequestStorageType;

@Repository
public interface ProcurementRequestStorageTypeRepository extends JpaRepository<ProcurementRequestStorageType, Long> {

  List<ProcurementRequestStorageType> findAllByProcurementRequestId(Long procurementRequestId);
}
