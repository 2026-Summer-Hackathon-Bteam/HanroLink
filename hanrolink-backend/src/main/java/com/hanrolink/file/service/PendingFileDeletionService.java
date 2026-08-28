package com.hanrolink.file.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.file.repository.PendingFileDeletionRepository;
import com.hanrolink.file.entity.PendingFileDeletion;

@Service
public class PendingFileDeletionService {

  private final PendingFileDeletionRepository pendingFileDeletionRepository;

  public PendingFileDeletionService(
    PendingFileDeletionRepository pendingFileDeletionRepository
  ) {
    this.pendingFileDeletionRepository = pendingFileDeletionRepository;
  }

  @Transactional
  public void create(String storageKey) {
    pendingFileDeletionRepository.save(new PendingFileDeletion(storageKey));
  }
}
