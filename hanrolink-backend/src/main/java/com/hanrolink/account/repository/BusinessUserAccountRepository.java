package com.hanrolink.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.repository.projection.AuthenticatedBusinessUserAccountProjection;
import com.hanrolink.account.repository.projection.BusinessAccessProjection;
import com.hanrolink.account.repository.projection.AuthorizationContextProjection;

@Repository
public interface BusinessUserAccountRepository extends JpaRepository<BusinessUserAccount, Long> {

  Optional<BusinessUserAccount> findByIdentityProviderSubject(String identityProviderSubject);

  boolean existsByIdentityProviderSubject(String identityProviderSubject);

  Optional<BusinessUserAccount> findByBusinessId(Long businessId);

  @Query("""
    SELECT businessUserAccount.id
    FROM BusinessUserAccount businessUserAccount
    WHERE businessUserAccount.identityProviderSubject
      = :identityProviderSubject
    """)
  Optional<Long> findIdByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject
  );

  @Query("""
    SELECT new com.hanrolink.account.repository.projection.AuthorizationContextProjection(
      business.role,
      business.reviewStatus
    )
    FROM BusinessUserAccount businessUserAccount
    JOIN Business business
      ON business.id = businessUserAccount.businessId
    WHERE businessUserAccount.identityProviderSubject
      = :identityProviderSubject
    """)
  Optional<AuthorizationContextProjection> findAuthorizationContextByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject
  );

  @Query("""
    SELECT new com.hanrolink.account.repository.projection.BusinessAccessProjection(
      business.publicId,
      business.role
    )
    FROM BusinessUserAccount businessUserAccount
    JOIN Business business
      ON business.id = businessUserAccount.businessId
    WHERE businessUserAccount.identityProviderSubject
      = :identityProviderSubject
    """)
  Optional<BusinessAccessProjection> findBusinessAccessByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject
  );

  @Query("""
    SELECT new com.hanrolink.account.repository.projection.AuthenticatedBusinessUserAccountProjection(
      businessUserAccount.id,
      business.role
    )
    FROM BusinessUserAccount businessUserAccount
    JOIN Business business
      ON business.id = businessUserAccount.businessId
    WHERE businessUserAccount.identityProviderSubject
      = :identityProviderSubject
    """)
  Optional<AuthenticatedBusinessUserAccountProjection>
    findAuthenticatedAccountByIdentityProviderSubject(
      @Param("identityProviderSubject")
      String identityProviderSubject
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
