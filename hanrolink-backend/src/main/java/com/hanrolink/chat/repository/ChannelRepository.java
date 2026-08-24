package com.hanrolink.chat.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.chat.entity.Channel;
import com.hanrolink.chat.repository.projection.MyChatListProjection;
import com.hanrolink.chat.repository.projection.MyChatParticipantContextProjection;
import com.hanrolink.chat.repository.projection.MyChatOverviewProjection;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

  @Query("""
    SELECT COUNT(channel.id) > 0
    FROM Channel channel
    JOIN BusinessUserAccount viewerAccount
      ON (
        viewerAccount.id = channel.supplierAccountId
        OR viewerAccount.id = channel.buyerAccountId
      )
    WHERE channel.publicId = :channelPublicId
      AND viewerAccount.identityProviderSubject = :identityProviderSubject
    """)
  boolean existsByPublicIdAndIdentityProviderSubject(
    @Param("channelPublicId")
    UUID channelPublicId,
    @Param("identityProviderSubject")
    String identityProviderSubject
  );

  @Query("""
    SELECT new com.hanrolink.chat.repository.projection.MyChatParticipantContextProjection(
      channel.id,
      viewerAccount.id
    )
    FROM Channel channel
    JOIN BusinessUserAccount viewerAccount
      ON (
        viewerAccount.id = channel.supplierAccountId
        OR viewerAccount.id = channel.buyerAccountId
      )
    WHERE channel.publicId = :channelPublicId
      AND viewerAccount.identityProviderSubject = :identityProviderSubject
    """)
  Optional<MyChatParticipantContextProjection>
    findParticipantContextByPublicIdAndIdentityProviderSubject(
      @Param("channelPublicId")
      UUID channelPublicId,
      @Param("identityProviderSubject")
      String identityProviderSubject
    );

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

  @Query("""
    SELECT new com.hanrolink.chat.repository.projection.MyChatListProjection(
      channel.publicId,
      channel.name,
      COALESCE(
        MAX(message.createdAt),
        channel.createdAt
      )
    )
    FROM Channel channel
    JOIN BusinessUserAccount viewerAccount
      ON (
        viewerAccount.id = channel.supplierAccountId
        OR viewerAccount.id = channel.buyerAccountId
      )
    LEFT JOIN Message message
      ON message.channelId = channel.id
    WHERE viewerAccount.identityProviderSubject = :identityProviderSubject
    GROUP BY
      channel.id,
      channel.publicId,
      channel.name,
      channel.createdAt
    ORDER BY
      COALESCE(
        MAX(message.createdAt),
        channel.createdAt
      ) DESC,
      channel.id DESC
    """)
  List<MyChatListProjection> findAllByParticipantIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject
  );
}
