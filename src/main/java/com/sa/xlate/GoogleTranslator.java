package com.sa.xlate;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.PrintStream;

public class GoogleTranslator {

  public static void translateTextWithOptionsAndModel(
      String sourceText,
      String sourceLang,
      String targetLang,
      PrintStream out) {

    Translate translate = createTranslateService();
    TranslateOption srcLang = TranslateOption.sourceLanguage(sourceLang);
    TranslateOption tgtLang = TranslateOption.targetLanguage(targetLang);


    // Use translate `model` parameter with `base` and `nmt` options.
    TranslateOption model = TranslateOption.model("nmt");

    Translation translation = translate.translate(sourceText, srcLang, tgtLang, model);
    out.printf("Source Text:\n\tLang: %s, Text: %s\n", sourceLang, sourceText);
    out.printf("TranslatedText:\n\tLang: %s, Text: %s\n", targetLang,
        translation.getTranslatedText());
  }


  /**
   * Translate the source text from source to target language.
   *
   * @param sourceText source text to be translated
   * @param sourceLang source language of the text
   * @param targetLang target language of translated text
   * @param out        print stream
   */
  public static void translateTextWithOptions(
      String sourceText,
      String sourceLang,
      String targetLang,
      PrintStream out) {

    Translate translate = createTranslateService();
    TranslateOption srcLang = TranslateOption.sourceLanguage(sourceLang);
    TranslateOption tgtLang = TranslateOption.targetLanguage(targetLang);

    Translation translation = translate.translate(sourceText, srcLang, tgtLang);
    out.printf("Source Text:\n\tLang: %s, Text: %s\n", sourceLang, sourceText);
    out.printf("TranslatedText:\n\tLang: %s, Text: %s\n", targetLang,
        translation.getTranslatedText());
  }

  public static String translateText(String sourceLang, String targetLang, String sourceText) {
    Translate translate = createTranslateService();
    TranslateOption sourceLangOption = TranslateOption.sourceLanguage(sourceLang);
    TranslateOption targetLangOption = TranslateOption.targetLanguage(targetLang);
    Translation translation = translate.translate(sourceText, sourceLangOption, targetLangOption);
    return translation.getTranslatedText();



  }

  /**
   * Create Google Translate API Service.
   *
   * @return Google Translate Service
   */
  public static Translate createTranslateService() {
    return TranslateOptions.newBuilder().build().getService();
  }

  public static void main(String[] args) {
    String sourceLang = args[0];
    String targetLang = args[1];
    String text = args[2];

    GoogleTranslator.translateTextWithOptionsAndModel(text, sourceLang, targetLang, System.out);
  }

}
