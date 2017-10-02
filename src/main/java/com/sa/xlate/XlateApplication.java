package com.sa.xlate;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
@EnableCaching
public class XlateApplication implements CommandLineRunner {

  private JsonFactory factory = new JsonFactory();

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
    JsonTranslator jsonTranslator = JsonTranslator.builder()
        .translatorService(translatorService)
        .sourceLang(sourceLang)
        .targetLang(targetLang)
        .build();
    jsonTranslator.translate();
  }

}
