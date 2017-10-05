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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = XlateApplication.class,
    initializers = ConfigFileApplicationContextInitializer.class)
public class XlateApplicationTests {

  public static final String TARGET_LANG = "xx";
  @Autowired
  private XlateApplication xlateApplication;

  @MockBean
  private Translate translate;

  @Test
  public void translateToXX() {
    when(translate.translate(anyString(), Matchers.anyVararg() )).thenAnswer(TranslateFixture::getMockAnswer);
    xlateApplication.translateJsonFile("en", TARGET_LANG);
  }

  private Translation getMockAnswer(InvocationOnMock invocation) {
    String text = invocation.getArgumentAt(0, String.class);

    Translation mock = Mockito.mock(Translation.class);
    when(mock.getTranslatedText()).thenReturn( TARGET_LANG + "-" + text);
    return mock;
  }

}
