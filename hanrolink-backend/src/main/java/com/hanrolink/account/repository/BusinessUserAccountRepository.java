package com.hanrolink.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.repository.projection.BusinessUserAccountAccessScopeProjection;
import com.hanrolink.account.repository.projection.FileUploadContextProjection;
import com.hanrolink.account.repository.projection.BusinessProfileAccessProjection;
import com.hanrolink.account.repository.projection.BusinessUserAccountAuthorizationProjection;

@Repository
public interface BusinessUserAccountRepository extends JpaRepository<BusinessUserAccount, Long> {

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
    SELECT businessUserAccount.businessId
    FROM BusinessUserAccount businessUserAccount
    WHERE businessUserAccount.identityProviderSubject
      = :identityProviderSubject
    """)
  Optional<Long> findBusinessIdByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject
  );

  @Query("""
    SELECT new com.hanrolink.account.repository.projection.BusinessUserAccountAuthorizationProjection(
      business.role,
      business.reviewStatus
    )
    FROM BusinessUserAccount businessUserAccount
    JOIN Business business
      ON business.id = businessUserAccount.businessId
    WHERE businessUserAccount.identityProviderSubject
      = :identityProviderSubject
    """)
  Optional<BusinessUserAccountAuthorizationProjection> findAuthorizationByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject
  );

  @Query("""
    SELECT new com.hanrolink.account.repository.projection.BusinessProfileAccessProjection(
      business.publicId,
      business.role
    )
    FROM BusinessUserAccount businessUserAccount
    JOIN Business business
      ON business.id = businessUserAccount.businessId
    WHERE businessUserAccount.identityProviderSubject
      = :identityProviderSubject
    """)
  Optional<BusinessProfileAccessProjection> findBusinessProfileAccessByIdentityProviderSubject(
    @Param("identityProviderSubject")
    String identityProviderSubject
  );

  @Query("""
    SELECT new com.hanrolink.account.repository.projection.BusinessUserAccountAccessScopeProjection(
      businessUserAccount.id,
      business.id,
      business.role
    )
    FROM BusinessUserAccount businessUserAccount
    JOIN Business business
      ON business.id = businessUserAccount.businessId
    WHERE businessUserAccount.identityProviderSubject
      = :identityProviderSubject
    """)
  Optional<BusinessUserAccountAccessScopeProjection>
    findAccessScopeByIdentityProviderSubject(
      @Param("identityProviderSubject")
      String identityProviderSubject
    );

  @Query("""
    SELECT new com.hanrolink.account.repository.projection.FileUploadContextProjection(
      businessUserAccount.id,
      business.publicId
    )
    FROM BusinessUserAccount businessUserAccount
    JOIN Business business
      ON business.id = businessUserAccount.businessId
    WHERE businessUserAccount.identityProviderSubject
      = :identityProviderSubject
    """)
  Optional<FileUploadContextProjection>
    findFileUploadContextByIdentityProviderSubject(
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

  @Query("""
    SELECT business.name
    FROM BusinessUserAccount businessUserAccount
    JOIN Business business
      ON business.id = businessUserAccount.businessId
    WHERE businessUserAccount.id = :businessUserAccountId
    """)
  Optional<String> findBusinessNameById(
    @Param("businessUserAccountId")
    Long businessUserAccountId
  );
}
