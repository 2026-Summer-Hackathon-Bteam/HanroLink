package com.hanrolink.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
      // 開発中なのでCSRFは無効
      .csrf(csrf -> csrf.disable())

      // CORSを有効化
      .cors(Customizer.withDefaults())

      // 全リクエストを許可
      .authorizeHttpRequests(auth -> auth
        .anyRequest().permitAll()
      );

    return http.build();
  }
}
