package com.hanrolink.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanrolink.chat.entity.MessageFile;

@Repository
public interface MessageFileRepository extends JpaRepository<MessageFile, Long> {}
