package com.hanrolink.businessapproval.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.account.enums.BusinessUserAccountReviewStatus;
import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.businessapproval.response.AdminBusinessApprovalListResponse;

@Service
public class AdminBusinessApprovalService {

  private final BusinessUserAccountRepository businessUserAccountRepository;

  public AdminBusinessApprovalService(
    BusinessUserAccountRepository businessUserAccountRepository
  ) {
    this.businessUserAccountRepository = businessUserAccountRepository;
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
}
