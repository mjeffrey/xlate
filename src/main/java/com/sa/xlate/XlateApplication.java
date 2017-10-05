package com.sa.xlate;

import com.fasterxml.jackson.core.JsonFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.sa.xlate.JsonTranslator.*;
import static com.sa.xlate.JsonTranslator.createLanguageParser;
import static com.sa.xlate.JsonTranslator.langToFilename;

@SpringBootApplication
@EnableCaching
public class XlateApplication implements CommandLineRunner {

  @Autowired
  private TranslatorService translatorService;

  public static void main(String[] args) {
    SpringApplication.run(XlateApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    String sourceLang = "en";
    translateJsonFile(sourceLang, "fr");
    translateJsonFile(sourceLang, "nl");
  }

  @SneakyThrows
  public void translateJsonFile(String sourceLang, String targetLang) {
    JsonTranslator jsonTranslator = builder()
        .translatorService(translatorService)
        .languageParser(createParser(sourceLang))
        .languageGenerator(createGenerator(targetLang))
        .build();
    jsonTranslator.translate();
  }


  private LanguageParser createParser(String sourceLang) throws IOException {
    InputStream stream = this.getClass().getResourceAsStream("/" + langToFilename(sourceLang));
    return createLanguageParser(sourceLang, stream);
  }

  private LanguageGenerator createGenerator(String targetLang) throws IOException {
    OutputStream outputStream = new FileOutputStream(langToFilename(targetLang));
    return createLanguageGenerator(targetLang, outputStream);
  }


}
