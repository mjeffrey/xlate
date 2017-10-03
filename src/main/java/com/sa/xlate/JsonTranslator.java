package com.sa.xlate;

import com.fasterxml.jackson.core.*;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.fasterxml.jackson.core.JsonTokenId.ID_STRING;

@Slf4j
@Builder
class JsonTranslator {
  private final JsonFactory factory = new JsonFactory();

  private TranslatorService translatorService;
  private LanguageGenerator languageGenerator;
  private LanguageParser languageParser;

  @SneakyThrows
  public void translate() {
    JsonParser parser = languageParser.getJsonParser();
    JsonGenerator generator = languageGenerator.getJsonGenerator();
    while (parser.nextToken() != null) {
      JsonToken jsonToken = parser.currentToken();
      if (jsonToken.id() == ID_STRING) {
        String text = parser.getText();
        text = translateText(languageParser.getSourcelanguage(), languageGenerator.getTargetLanguage(), text);
        generator.writeString(text);
      } else {
        generator.copyCurrentEvent(parser);
      }
    }
    languageParser.close();
    languageGenerator.close();
  }

  private String translateText(String sourceLang, String targetlang, String text) {
    if (StringUtils.isBlank(text)) {
      return "";
    }
    String translateText = translatorService.translateText(sourceLang, targetlang, text);
    log.info("{} ===> {}", text, translateText);
    return translateText;
  }

}
