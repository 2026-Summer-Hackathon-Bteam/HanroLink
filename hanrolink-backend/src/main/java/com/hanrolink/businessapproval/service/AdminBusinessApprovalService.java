package com.hanrolink.businessapproval.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.enums.BusinessUserAccountReviewStatus;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.business.entity.Business;
import com.hanrolink.business.repository.BusinessRepository;
import com.hanrolink.businessapproval.response.AdminBusinessApprovalDetailResponse;
import com.hanrolink.businessapproval.response.AdminBusinessApprovalListResponse;
import com.hanrolink.businessapproval.response.component.BusinessApprovalAccountResponse;
import com.hanrolink.businessapproval.response.component.BusinessApprovalBusinessResponse;

@Service
public class AdminBusinessApprovalService {

  private final BusinessUserAccountRepository businessUserAccountRepository;

  private final BusinessRepository businessRepository;

  public AdminBusinessApprovalService(
    BusinessUserAccountRepository businessUserAccountRepository,
    BusinessRepository businessRepository
  ) {
    this.businessUserAccountRepository = businessUserAccountRepository;
    this.businessRepository = businessRepository;
  }

  /**
   * 審査待ちの事業者ユーザーアカウント一覧を取得する
   * @return 審査待ちの事業者ユーザーアカウント一覧
   */
  @Transactional(readOnly = true)
  public List<AdminBusinessApprovalListResponse> listPending() {
    return businessUserAccountRepository
      .findBusinessUserAccountSummariesByReviewStatus(
        BusinessUserAccountReviewStatus.PENDING
      );
  }

  /**
   * 審査対象の詳細情報を取得する
   * @param businessUserAccountId 事業者ユーザーアカウントの公開識別子
   * @return 審査対象の詳細情報
   */
  @Transactional(readOnly = true)
  public AdminBusinessApprovalDetailResponse getDetail(
    UUID businessUserAccountId
  ) {
    BusinessUserAccount businessUserAccount =
      businessUserAccountRepository.findByPublicId(businessUserAccountId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    Business business =
      businessRepository.findById(businessUserAccount.getBusinessId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    BusinessApprovalAccountResponse businessApprovalAccountResponse =
      new BusinessApprovalAccountResponse(
        businessUserAccountId,
        businessUserAccount.getRole(),
        businessUserAccount.getReviewStatus(),
        businessUserAccount.getLastName(),
        businessUserAccount.getFirstName(),
        businessUserAccount.getLastNameKana(),
        businessUserAccount.getFirstNameKana(),
        businessUserAccount.getPhoneNumber(),
        businessUserAccount.getEmail(),
        businessUserAccount.getCreatedAt()
      );

    BusinessApprovalBusinessResponse businessApprovalBusinessResponse =
      new BusinessApprovalBusinessResponse(
        business.getName(),
        business.getNameKana(),
        business.getWebsiteUrl(),
        business.getAddressPostalCode(),
        business.getAddressPrefecture(),
        business.getAddressMunicipalityStreet(),
        business.getAddressBuilding(),
        business.getPhoneNumber()
      );

    return new AdminBusinessApprovalDetailResponse(
      businessApprovalAccountResponse,
      businessApprovalBusinessResponse
    );
  }

  /**
   * 審査対象の事業者ユーザーアカウントを承認する
   * @param businessUserAccountId 事業者ユーザーアカウントの公開識別子
   */
  @Transactional
  public void approve(
    UUID businessUserAccountId
  ) {
    BusinessUserAccount businessUserAccount =
      businessUserAccountRepository.findByPublicId(businessUserAccountId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    businessUserAccount.approve();
  }
}
