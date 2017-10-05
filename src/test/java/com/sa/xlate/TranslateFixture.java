package com.sa.xlate;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class TranslateFixture {

  public static Translate getTranslateMock(){
    Translate translate = Mockito.mock(Translate.class);
    when(translate.translate(anyString(), Matchers.anyVararg() )).thenAnswer(TranslateFixture::getMockAnswer);
    return translate;
  }

  public static TranslatorService getTranslatorServiceMock(){
    return new TranslatorService(getTranslateMock());
  }

  public static Translation getMockAnswer(InvocationOnMock invocation) {
    String text = invocation.getArgumentAt(0, String.class);

    Translation mock = Mockito.mock(Translation.class);
    when(mock.getTranslatedText()).thenReturn( "target" + "-" + text);
    return mock;
  }
}
