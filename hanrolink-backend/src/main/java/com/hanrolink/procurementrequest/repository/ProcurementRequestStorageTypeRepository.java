package com.hanrolink.procurementrequest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.procurementrequest.entity.ProcurementRequestStorageType;
import com.hanrolink.product.enums.StorageType;

@Repository
public interface ProcurementRequestStorageTypeRepository extends JpaRepository<ProcurementRequestStorageType, Long> {

  List<ProcurementRequestStorageType> findAllByProcurementRequestId(Long procurementRequestId);

  @Query("""
    SELECT procurementRequestStorageType.storageType
    FROM ProcurementRequestStorageType procurementRequestStorageType
    WHERE procurementRequestStorageType.procurementRequestId =
      :procurementRequestId
    """)
  List<StorageType> findStorageTypesByProcurementRequestId(
    @Param("procurementRequestId")
    Long procurementRequestId
  );
}
