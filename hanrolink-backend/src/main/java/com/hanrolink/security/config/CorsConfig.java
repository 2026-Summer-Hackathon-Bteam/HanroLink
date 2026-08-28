package com.hanrolink.security.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  private final String allowedOrigin;

  public CorsConfig(
    @Value("${app.security.cors.allowed-origin}")
    String allowedOrigin
  ) {
    this.allowedOrigin = allowedOrigin;
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(
      List.of(allowedOrigin)
    );

    config.setAllowedMethods(List.of(
      "GET",
      "POST",
      "PUT",
      "PATCH",
      "DELETE",
      "OPTIONS"
    ));

    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source =
      new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", config);

    return source;
  }
}