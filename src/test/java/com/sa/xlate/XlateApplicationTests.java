package com.sa.xlate;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import lombok.experimental.Delegate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = XlateApplication.class)
@ContextConfiguration(classes = XlateApplication.class,
    initializers = ConfigFileApplicationContextInitializer.class)
public class XlateApplicationTests {

  @Autowired
  private XlateApplication xlateApplication;

  @MockBean
  Translate translate;

  @Mock
  Translation translation;

  public static class TestTranslate implements Translate {
    @Delegate(excludes = MockedTranslate.class)
    Translate translate = Mockito.mock(Translate.class);

    private interface MockedTranslate {
      Translation translate(String text, Translate.TranslateOption... options);
    }

    public Translation translate(String text, Translate.TranslateOption... options) {
      Translation mock = Mockito.mock(Translation.class);
      when(mock.getTranslatedText()).thenReturn("XX-" + text);
      return mock;
    }
  }

  @Test
  public void contextLoads() {

    when(translate.translate(anyString(), Matchers.anyVararg() )).thenAnswer(this::getMockAnswer);
    xlateApplication.translateJsonFile("en", "xx");
  }

  private Translation getMockAnswer(InvocationOnMock invocation) {
    String text = invocation.getArgumentAt(0, String.class);
    Translation mock = Mockito.mock(Translation.class);
    when(mock.getTranslatedText()).thenReturn( "XX-" + text);
    return mock;
  }

}
