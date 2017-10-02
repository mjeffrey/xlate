package com.sa.xlate;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

public class ParseFileTest {
  @Test
  public void name() throws Exception {
    JsonFactory factory = new JsonFactory();
    String from = "en";
    String to = "fr";

    InputStream stream = this.getClass().getResourceAsStream("/" + langToFilename(from));
    if (stream == null) {
      throw new NullPointerException();
    }
    JsonParser parser = factory.createParser(stream);
    JsonGenerator generator = factory.createGenerator(new File(langToFilename(to)), JsonEncoding.UTF8);
    JsonTranslator jsonTranslator = JsonTranslator.builder().sourceLang(from).targetLang(to).build();
    jsonTranslator.translate();

  }

  private String langToFilename(String lang) {
    return lang + ".json";
  }

}
