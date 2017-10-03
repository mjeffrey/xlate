package com.sa.xlate;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

@Service
@Slf4j
@CacheConfig(cacheNames = "translation")
public class TranslatorService {

  @Autowired
  private Translate translate;

  @Cacheable
  public String translateText(String sourceLang, String targetLang, String sourceText) {
    TranslateOption sourceLangOption = TranslateOption.sourceLanguage(sourceLang);
    TranslateOption targetLangOption = TranslateOption.targetLanguage(targetLang);
    log.info("Not cached!!!! {} ", sourceText);

    Translation translation = translate.translate(sourceText, sourceLangOption, targetLangOption);
    return translation.getTranslatedText();
}


}
