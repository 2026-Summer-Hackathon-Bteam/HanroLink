package com.hanrolink.account.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.business.entity.Business;

@Repository
public interface BusinessUserAccountRepository extends JpaRepository<BusinessUserAccount, Long> {

  Optional<BusinessUserAccount> findByIdentityProviderSubject(String identityProviderSubject);

  boolean existsByIdentityProviderSubject(String identityProviderSubject);

  @Query("""
    SELECT business
    FROM Business business
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.businessId = business.id
    WHERE businessUserAccount.publicId = :businessUserAccountPublicId
    """)
  Optional<Business> findBusinessByBusinessUserAccountPublicId(
    @Param("businessUserAccountPublicId")
    UUID businessUserAccountPublicId
  );

  @Query("""
    SELECT business.name
    FROM Business business
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.businessId = business.id
    WHERE businessUserAccount.identityProviderSubject = :identityProviderSubject
    """)
  Optional<String> findBusinessNameByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject
  );
}
