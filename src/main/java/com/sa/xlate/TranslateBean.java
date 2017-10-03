package com.sa.xlate;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TranslateBean {

  @Bean
  public Translate createTranslateService() {
    return TranslateOptions.newBuilder().build().getService();
  }

}
