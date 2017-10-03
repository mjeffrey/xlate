package com.sa.xlate;

import com.fasterxml.jackson.core.JsonParser;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;

@Builder
@Getter
public class LanguageParser {
  private String sourcelanguage;
  private JsonParser jsonParser;

  public void close() throws IOException {
    jsonParser.close();
  }
}
