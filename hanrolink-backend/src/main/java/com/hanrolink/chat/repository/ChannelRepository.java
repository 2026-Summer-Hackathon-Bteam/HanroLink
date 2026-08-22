package com.hanrolink.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanrolink.chat.entity.Channel;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {}
