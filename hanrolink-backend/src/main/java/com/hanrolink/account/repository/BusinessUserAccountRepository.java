package com.hanrolink.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanrolink.account.entity.BusinessUserAccount;

@Repository
public interface BusinessUserAccountRepository extends JpaRepository<BusinessUserAccount, Long> {

  Optional<BusinessUserAccount> findByIdentityProviderSubject(String identityProviderSubject);

  boolean existsByIdentityProviderSubject(String identityProviderSubject);
}
