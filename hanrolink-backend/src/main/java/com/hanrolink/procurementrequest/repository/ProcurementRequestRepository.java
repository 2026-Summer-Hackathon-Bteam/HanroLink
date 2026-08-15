package com.hanrolink.procurementrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanrolink.procurementrequest.entity.ProcurementRequest;

@Repository
public interface ProcurementRequestRepository extends JpaRepository<ProcurementRequest, Long> {}
