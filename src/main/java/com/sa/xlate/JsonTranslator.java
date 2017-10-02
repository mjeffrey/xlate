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

  private String sourceLang;
  private String targetLang;
  private TranslatorService translatorService;

  @SneakyThrows
  public void translate() {
    JsonParser parser = createJsonParser();
    JsonGenerator generator = createJsonGenerator();
    while (parser.nextToken() != null) {
      JsonToken jsonToken = parser.currentToken();
      if (jsonToken.id() == ID_STRING) {
        String text = parser.getText();
        text = translateText(sourceLang, targetLang, text);
        generator.writeString(text);
      } else {
        generator.copyCurrentEvent(parser);
      }
    }
    parser.close();
    generator.close();

  }

  private JsonGenerator createJsonGenerator() throws IOException {
    JsonGenerator generator = factory.createGenerator(new File(langToFilename(targetLang)), JsonEncoding.UTF8);
    generator.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);
    generator.useDefaultPrettyPrinter();
    return generator;
  }

  private JsonParser createJsonParser() throws IOException {
    InputStream stream = this.getClass().getResourceAsStream("/" + langToFilename(sourceLang));
    if (stream == null) {
      throw new NullPointerException();
    }
    JsonParser parser = factory.createParser(stream);
    parser.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
    return parser;
  }


  private String langToFilename(String lang) {
    return lang + ".json";
  }

  private String translateText(String sourceLang, String targetlang, String text) {
    if (StringUtils.isBlank(text)) {
      return "";
    }
    log.info("called: " + text);
    return translatorService.translateText(sourceLang, targetlang, text);
  }

}
