package com.hanrolink.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanrolink.file.entity.PendingFileDeletion;

@Repository
public interface PendingFileDeletionRepository extends JpaRepository<PendingFileDeletion, Long> {}
