package com.sa.xlate;

import org.junit.Test;

import java.io.*;

import static com.sa.xlate.JsonTranslator.langToFilename;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonTranslatorTest {

  private TranslatorService translatorService = TranslateFixture.getTranslatorServiceMock();
  private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  @Test
  public void testTranslate() throws Exception {
    JsonTranslator jsonTranslator = JsonTranslator.builder()
        .translatorService(translatorService)
        .languageParser(createParser("en"))
        .languageGenerator(createGenerator("yy"))
        .build();
    jsonTranslator.translate();
    System.out.println(outputStream.toString());
  }

  private LanguageParser createParser(String sourceLang) throws IOException {
    InputStream stream = this.getClass().getResourceAsStream("/" + langToFilename(sourceLang));
    return JsonTranslator.createLanguageParser(sourceLang, stream);
  }

  private LanguageGenerator createGenerator(String targetLang) throws IOException {
    return JsonTranslator.createLanguageGenerator(targetLang, outputStream);
  }

}
