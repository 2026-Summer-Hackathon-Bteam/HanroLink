package com.hanrolink.businessapproval.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.business.entity.Business;
import com.hanrolink.business.enums.BusinessReviewStatus;
import com.hanrolink.business.repository.BusinessRepository;
import com.hanrolink.businessapproval.response.AdminBusinessApprovalDetailResponse;
import com.hanrolink.businessapproval.response.AdminBusinessApprovalListResponse;
import com.hanrolink.businessapproval.response.component.BusinessApprovalAccountResponse;
import com.hanrolink.businessapproval.response.component.BusinessApprovalBusinessResponse;

@Service
public class AdminBusinessApprovalService {

  private final BusinessRepository businessRepository;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  public AdminBusinessApprovalService(
    BusinessRepository businessRepository,
    BusinessUserAccountRepository businessUserAccountRepository
  ) {
    this.businessRepository = businessRepository;
    this.businessUserAccountRepository = businessUserAccountRepository;
  }

  /**
   * 審査待ちの事業者一覧を取得する
   * @return 審査待ちの事業者一覧
   */
  @Transactional(readOnly = true)
  public List<AdminBusinessApprovalListResponse> listPending() {
    return businessRepository
      .findApprovalListByReviewStatus(
        BusinessReviewStatus.PENDING
      );
  }

  /**
   * 審査対象の詳細情報を取得する
   * @param businessPublicId 事業者の公開識別子
   * @return 審査対象の詳細情報
   */
  @Transactional(readOnly = true)
  public AdminBusinessApprovalDetailResponse getDetail(
    UUID businessPublicId
  ) {
    Business business =
      businessRepository.findByPublicId(businessPublicId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    BusinessUserAccount businessUserAccount =
      businessUserAccountRepository.findByBusinessId(business.getId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    BusinessApprovalBusinessResponse businessApprovalBusinessResponse =
      new BusinessApprovalBusinessResponse(
        business.getPublicId(),
        business.getRole(),
        business.getReviewStatus(),
        business.getName(),
        business.getNameKana(),
        business.getWebsiteUrl(),
        business.getAddressPostalCode(),
        business.getAddressPrefecture(),
        business.getAddressMunicipalityStreet(),
        business.getAddressBuilding(),
        business.getPhoneNumber(),
        business.getCreatedAt()
      );

    BusinessApprovalAccountResponse businessApprovalAccountResponse =
      new BusinessApprovalAccountResponse(
        businessUserAccount.getLastName(),
        businessUserAccount.getFirstName(),
        businessUserAccount.getLastNameKana(),
        businessUserAccount.getFirstNameKana(),
        businessUserAccount.getPhoneNumber(),
        businessUserAccount.getEmail()
      );

    return new AdminBusinessApprovalDetailResponse(
      businessApprovalBusinessResponse,
      businessApprovalAccountResponse
    );
  }

  /**
   * 審査対象の事業者を承認する
   * @param businessPublicId 事業者の公開識別子
   */
  @Transactional
  public void approve(
    UUID businessPublicId
  ) {
    Business business =
      businessRepository.findByPublicId(businessPublicId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    business.approve();
  }
}
