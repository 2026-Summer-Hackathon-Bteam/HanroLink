package com.hanrolink.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.security.authorization.AccountGrantedAuthoritiesConverter;

@Profile("cognito")
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
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(
          "/actuator/health",
          "/v3/api-docs/**",
          "/swagger-ui/**",
          "/swagger-ui.html"
        ).permitAll()
        .requestMatchers(
          HttpMethod.GET,
          ProductApi.V1.BASE_PUBLIC
        ).permitAll()
        .anyRequest().authenticated()
      )
      .oauth2ResourceServer(resourceServer ->
        resourceServer.jwt(jwt ->
          jwt.jwtAuthenticationConverter(
            jwtAuthenticationConverter
          )
        )
      );

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
