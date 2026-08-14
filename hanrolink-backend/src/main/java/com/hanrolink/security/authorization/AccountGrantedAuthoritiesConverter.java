package com.hanrolink.security.authorization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.account.repository.projection.AuthorizationContextProjection;
import com.hanrolink.security.authorization.enums.JwtAccountRole;

@Component
public class AccountGrantedAuthoritiesConverter
  implements Converter<Jwt, Collection<GrantedAuthority>> {

  private static final String ROLE_PREFIX = "ROLE_";
  private static final String REVIEW_PREFIX = "REVIEW_";
  private static final String UNREGISTERED_AUTHORITY = "UNREGISTERED_BUSINESS_USER_ACCOUNT";

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
      .findAuthorizationContextByIdentityProviderSubject(jwt.getSubject())
      .map(this::convertAuthorizationContext)
      .orElseGet(() -> List.of(
        new SimpleGrantedAuthority(
          UNREGISTERED_AUTHORITY
        )
      ));
  }

  private Collection<GrantedAuthority> convertAuthorizationContext(
    AuthorizationContextProjection authorizationContext
  ) {
    List<GrantedAuthority> authorities = new ArrayList<>();

    if (authorizationContext.businessRole() != null) {
      authorities.add(
        new SimpleGrantedAuthority(
          ROLE_PREFIX + authorizationContext.businessRole().name()
        )
      );
    }

    if (authorizationContext.businessReviewStatus() != null) {
      authorities.add(
        new SimpleGrantedAuthority(
          REVIEW_PREFIX + authorizationContext.businessReviewStatus().name()
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
