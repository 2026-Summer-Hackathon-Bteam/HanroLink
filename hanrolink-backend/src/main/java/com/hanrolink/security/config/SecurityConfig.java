package com.hanrolink.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.security.authorization.AccountGrantedAuthoritiesConverter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(
    HttpSecurity http,
    JwtAuthenticationConverter jwtAuthenticationConverter
  ) throws Exception {

    http
      .csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults())
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      // Jwtが有効化した場合、下記の設定は変更すること
      // 全リクエストを許可
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.GET, ProductApi.V1.BASE_PUBLIC).permitAll()
        .anyRequest().permitAll()
      );

      // issuer-uri等を設定後、コメントアウトを解除すること
      // .oauth2ResourceServer(resourceServer ->
      //   resourceServer.jwt(jwt ->
      //     jwt.jwtAuthenticationConverter(
      //       jwtAuthenticationConverter
      //     )
      //   )
      // );

    return http.build();
  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter(
    AccountGrantedAuthoritiesConverter authoritiesConverter
  ) {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

    converter.setJwtGrantedAuthoritiesConverter(
      authoritiesConverter
    );

    converter.setPrincipalClaimName("sub");

    return converter;
  }
}
