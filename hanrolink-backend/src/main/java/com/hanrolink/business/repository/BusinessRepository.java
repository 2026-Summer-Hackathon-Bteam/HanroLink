package com.hanrolink.business.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanrolink.business.entity.Business;
import com.hanrolink.business.enums.BusinessReviewStatus;
import com.hanrolink.business.enums.BusinessRole;
import com.hanrolink.businessapproval.response.AdminBusinessApprovalListResponse;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

  Optional<Business> findByPublicId(UUID publicId);

  @Query("""
    SELECT business
    FROM Business business
    WHERE business.publicId = :publicId
      AND business.role = :role
      AND business.reviewStatus = com.hanrolink.business.enums.BusinessReviewStatus.APPROVED
    """)
  Optional<Business> findApprovedByPublicIdAndRole(
    @Param("publicId")
    UUID publicId,
    @Param("role")
    BusinessRole role
  );

  @Query("""
    SELECT new com.hanrolink.businessapproval.response.AdminBusinessApprovalListResponse(
      business.publicId,
      business.name,
      business.createdAt
    )
    FROM Business business
    WHERE business.reviewStatus = :reviewStatus
    ORDER BY business.createdAt ASC
    """)
  List<AdminBusinessApprovalListResponse>
    findApprovalListByReviewStatus(
      @Param("reviewStatus")
      BusinessReviewStatus reviewStatus
    );
}
