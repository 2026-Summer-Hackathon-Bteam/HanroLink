package com.hanrolink.infrastructure.cloudfront;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("cloudfront")
@Component
public class CloudFrontResourceUrlGenerator {

  private final String domainName;

  public CloudFrontResourceUrlGenerator(
    @Value("${app.storage.cloudfront.domain-name}")
    String domainName
  ) {
    this.domainName = domainName;
  }

  public String generate(
    String storageKey
  ) {
    return "https://"
      + domainName
      + "/"
      + storageKey;
  }
}
