package com.hanrolink.security.authorization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.hanrolink.account.entity.BusinessUserAccount;
import com.hanrolink.account.enums.JwtAccountRole;
import com.hanrolink.account.repository.BusinessUserAccountRepository;

@Component
public class AccountGrantedAuthoritiesConverter
  implements Converter<Jwt, Collection<GrantedAuthority>> {

  private static final String ROLE_PREFIX = "ROLE_";

  private static final String REVIEW_PREFIX = "REVIEW_";

  private final BusinessUserAccountRepository businessUserAccountRepository;

  AccountGrantedAuthoritiesConverter(
    BusinessUserAccountRepository businessUserAccountRepository
  ) {
    this.businessUserAccountRepository = businessUserAccountRepository;
  }

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    if (isAdmin(jwt)) {
      return List.of(
        new SimpleGrantedAuthority(
          ROLE_PREFIX + JwtAccountRole.ADMIN.name()
        )
      );
    }

    return businessUserAccountRepository
      .findByIdentityProviderSubject(jwt.getSubject())
      .map(this::convertAccount)
      .orElseGet(() -> List.of(
        new SimpleGrantedAuthority(
          "UNREGISTERED_BUSINESS_USER_ACCOUNT"
        )
      ));
  }

  private Collection<GrantedAuthority> convertAccount(
    BusinessUserAccount businessUserAccount
  ) {
    List<GrantedAuthority> authorities = new ArrayList<>();

    if (businessUserAccount.getRole() != null) {
      authorities.add(
        new SimpleGrantedAuthority(
          ROLE_PREFIX + businessUserAccount.getRole().name()
        )
      );
    }

    if (businessUserAccount.getReviewStatus() != null) {
      authorities.add(
        new SimpleGrantedAuthority(
          REVIEW_PREFIX + businessUserAccount.getReviewStatus().name()
        )
      );
    }

    return authorities;
  }

  private boolean isAdmin(Jwt jwt) {
    List<String> groups = jwt.getClaimAsStringList("cognito:groups");

    return groups != null && groups.contains(JwtAccountRole.ADMIN.name());
  }
}
