package com.hanrolink.infrastructure.validation;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ApplicationTimeValidationConfig {

  private static final ZoneId APPLICATION_ZONE_ID = ZoneId.of("Asia/Tokyo");

  @Bean
  public LocalValidatorFactoryBean validator() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

    validator.setConfigurationInitializer(configuration ->
      configuration.clockProvider(
        () -> Clock.system(APPLICATION_ZONE_ID)
      )
    );

    return validator;
  }
}
