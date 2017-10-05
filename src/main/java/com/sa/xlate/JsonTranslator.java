package com.sa.xlate;

import com.fasterxml.jackson.core.*;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.fasterxml.jackson.core.JsonTokenId.ID_STRING;

@Slf4j
@Builder
class JsonTranslator {
  private static final JsonFactory factory = new JsonFactory();

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

  public static LanguageParser createLanguageParser(String sourceLang, InputStream inputStream) throws IOException {
    if (inputStream == null) {
      throw new NullPointerException();
    }
    JsonParser parser = factory.createParser(inputStream);
    parser.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
    return LanguageParser.builder().jsonParser(parser).sourcelanguage(sourceLang).build();
  }

  public static LanguageGenerator createLanguageGenerator(String targetLang, OutputStream outputStream) throws IOException {
    JsonGenerator generator = factory.createGenerator(outputStream, JsonEncoding.UTF8);
    generator.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);
    generator.useDefaultPrettyPrinter();
    return LanguageGenerator.builder().jsonGenerator(generator).targetLanguage(targetLang).build();
  }

}
