package com.hanrolink.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanrolink.file.entity.PendingFileUpload;

@Repository
public interface PendingFileUploadRepository extends JpaRepository<PendingFileUpload, Long> {}
