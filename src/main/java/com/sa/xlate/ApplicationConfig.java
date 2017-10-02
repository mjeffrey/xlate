package com.sa.xlate;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google")
@Getter
public class ApplicationConfig {
  private String credentialsFileName;
}
