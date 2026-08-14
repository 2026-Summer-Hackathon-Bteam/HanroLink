package com.hanrolink.account.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.enums.BusinessUserAccountRole;
import com.hanrolink.account.repository.projection.AuthenticatedBusinessUserAccountProjection;
import com.hanrolink.account.enums.BusinessUserAccountReviewStatus;
import com.hanrolink.business.entity.Business;
import com.hanrolink.businessapproval.response.AdminBusinessApprovalListResponse;

@Repository
public interface BusinessUserAccountRepository extends JpaRepository<BusinessUserAccount, Long> {

  Optional<BusinessUserAccount> findByIdentityProviderSubject(String identityProviderSubject);

  boolean existsByIdentityProviderSubject(String identityProviderSubject);

  Optional<BusinessUserAccount> findByPublicId(UUID publicId);

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
    SELECT new com.hanrolink.account.repository.projection.AuthenticatedBusinessUserAccountProjection(
      businessUserAccount.id,
      businessUserAccount.role
    )
    FROM BusinessUserAccount businessUserAccount
    WHERE businessUserAccount.identityProviderSubject
      = :identityProviderSubject
    """)
  Optional<AuthenticatedBusinessUserAccountProjection>
    findAuthenticatedAccountByIdentityProviderSubject(
      @Param("identityProviderSubject")
      String identityProviderSubject
    );

  @Query("""
    SELECT business
    FROM Business business
    JOIN BusinessUserAccount businessUserAccount
      ON businessUserAccount.businessId = business.id
    WHERE businessUserAccount.publicId = :businessUserAccountPublicId
      AND businessUserAccount.role = :businessUserAccountRole
    """)
  Optional<Business> findBusinessByBusinessUserAccountPublicIdAndRole(
    @Param("businessUserAccountPublicId")
    UUID businessUserAccountPublicId,

    @Param("businessUserAccountRole")
    BusinessUserAccountRole role
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
    SELECT new com.hanrolink.businessapproval.response.AdminBusinessApprovalListResponse(
      businessUserAccount.publicId,
      business.name,
      businessUserAccount.createdAt
    )
    FROM BusinessUserAccount businessUserAccount
    JOIN Business business
      ON business.id = businessUserAccount.businessId
    WHERE businessUserAccount.reviewStatus = :reviewStatus
    ORDER BY businessUserAccount.createdAt ASC
    """)
  List<AdminBusinessApprovalListResponse>
    findBusinessUserAccountSummariesByReviewStatus(
      @Param("reviewStatus")
      BusinessUserAccountReviewStatus reviewStatus
    );
}
