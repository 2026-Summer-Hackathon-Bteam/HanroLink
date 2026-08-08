package com.hanrolink.business.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.business.response.CurrentBusinessGetResponse;

@Service
public class CurrentBusinessService {

  private final BusinessUserAccountRepository businessUserAccountRepository;

  public CurrentBusinessService(
    BusinessUserAccountRepository businessUserAccountRepository
  ) {
    this.businessUserAccountRepository = businessUserAccountRepository;
  }

  /**
   * 指定されたユーザー識別子に紐づく事業者情報を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @return ユーザー識別子に紐づく事業者情報
   */
  @Transactional
  public CurrentBusinessGetResponse get(
    String identityProviderSubject
  ) {
    String businessName =
      businessUserAccountRepository
        .findBusinessNameByIdentityProviderSubject(
          identityProviderSubject
        )
        .orElseThrow(
          () -> new ResponseStatusException(
            HttpStatus.NOT_FOUND
          )
        );
    return new CurrentBusinessGetResponse(businessName);
  }
}
