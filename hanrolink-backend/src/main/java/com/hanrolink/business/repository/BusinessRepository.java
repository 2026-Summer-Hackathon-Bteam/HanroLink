package com.hanrolink.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanrolink.business.entity.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {}
