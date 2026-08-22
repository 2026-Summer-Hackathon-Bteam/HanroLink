package com.hanrolink.chat.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.chat.entity.Channel;
import com.hanrolink.chat.repository.projection.MyChatOverviewProjection;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

  @Query("""
    SELECT new com.hanrolink.chat.repository.projection.MyChatOverviewProjection(
      channel.name,
      counterpartyBusiness.name
    )
    FROM Channel channel
    JOIN BusinessUserAccount viewerAccount
      ON (
        viewerAccount.id = channel.supplierAccountId
        OR viewerAccount.id = channel.buyerAccountId
      )
    JOIN BusinessUserAccount counterpartyAccount
      ON (
        counterpartyAccount.id = channel.supplierAccountId
        OR counterpartyAccount.id = channel.buyerAccountId
      )
      AND counterpartyAccount.id <> viewerAccount.id
    JOIN Business counterpartyBusiness
      ON counterpartyBusiness.id = counterpartyAccount.businessId
    WHERE channel.publicId = :channelPublicId
      AND viewerAccount.identityProviderSubject = :identityProviderSubject
    """)
  Optional<MyChatOverviewProjection> findOverviewByPublicIdAndIdentityProviderSubject(
    @Param("channelPublicId")
    UUID channelPublicId,
    @Param("identityProviderSubject")
    String identityProviderSubject
  );
}
