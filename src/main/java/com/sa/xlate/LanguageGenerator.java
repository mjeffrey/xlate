package com.sa.xlate;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;

@Builder
@Getter
public class LanguageGenerator {
  private String targetLanguage;
  private JsonGenerator jsonGenerator;

  public void close() throws IOException {
    jsonGenerator.close();
  }
}
