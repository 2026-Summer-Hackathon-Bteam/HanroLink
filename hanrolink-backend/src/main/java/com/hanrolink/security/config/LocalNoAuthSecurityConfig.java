package com.hanrolink.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Profile("local-no-auth")
@Configuration
@EnableMethodSecurity
public class LocalNoAuthSecurityConfig {

  @Bean
  SecurityFilterChain localNoAuthSecurityFilterChain(
    HttpSecurity http
  ) throws Exception {

    http
      .csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults())
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      // 全リクエストを許可
      .authorizeHttpRequests(auth -> auth
        .anyRequest().permitAll()
      );

    return http.build();
  }
}
