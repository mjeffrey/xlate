package com.sa.xlate;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.io.*;

import static com.sa.xlate.JsonTranslator.*;
import static java.util.Arrays.asList;

@SpringBootApplication
@EnableCaching
@Slf4j
public class XlateApplication implements CommandLineRunner {

  @Autowired
  private TranslatorService translatorService;

  private String directory;

    public static void main(String[] args) {
    SpringApplication.run(XlateApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    log.info("Arguments {}", asList(args));
    if (args.length == 0) {
      log.error("Usage: <source_directory>" );
    }
    directory = args[0];
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
    InputStream stream = new FileInputStream(getFileName(sourceLang));
    return createLanguageParser(sourceLang, stream);
  }

    private String getFileName(String lang) {
        String baseName = langToFilename(lang);
        return directory == null ? baseName : directory + File.separator + baseName;
    }

    private LanguageGenerator createGenerator(String targetLang) throws IOException {
    OutputStream outputStream = new FileOutputStream(getFileName(targetLang));
    return createLanguageGenerator(targetLang, outputStream);
  }


}
