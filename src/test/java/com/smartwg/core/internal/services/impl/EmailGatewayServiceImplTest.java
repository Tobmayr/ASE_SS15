package com.smartwg.core.internal.services.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.HashSet;
import java.util.Properties;

import javax.mail.MessagingException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.smartwg.core.internal.services.EmailGatewayService;

/**
 * @author Kamil Sierzant (ks)
 */
@Test
public class EmailGatewayServiceImplTest {

  @InjectMocks
  private EmailGatewayService service;

  @Mock
  private MessageSource messageSource;
  @Mock
  private Properties properties;

  @BeforeMethod
  public void setUp() {
    service = new EmailGatewayServiceImpl();

    MockitoAnnotations.initMocks(this);

    when(properties.getProperty("mail.smtp.host")).thenReturn("smtp.gmail.com");
    when(properties.getProperty("mail.smtp.port")).thenReturn("465");
    when(properties.getProperty("mail.smtp.class")).thenReturn("javax.net.ssl.SSLSocketFactory");
    when(properties.getProperty("mail.smtp.auth")).thenReturn("true");
    when(properties.getProperty("mail.smtp.port")).thenReturn("465");
    when(properties.getProperty("mail.address")).thenReturn("fair.share.mail@gmail.com");
  }

  @Test(expectedExceptions = MessagingException.class)
  public void generateAndSend() throws MessagingException {
    final String subject = "Shopping list done";
    final String content = "Shopping list has been marked as done";
    final HashSet<String> recipients = new HashSet<String>() {
      {
        add("invalidEmailAddress");
      }
    };
    when(
        messageSource.getMessage("email.auto.generated.notice", new Object[] {content},
            LocaleContextHolder.getLocale())).thenReturn("email content");
    when(properties.getProperty("email.content.template")).thenReturn("start %NOTICE% end");

    service.generateAndSendEmail(subject, content, recipients);
  }

  public void generateAndSendEmailSucceed() throws MessagingException {
    final String subject = "Shopping list done";
    final String content = "Shopping list has been marked as done";
    final HashSet<String> recipients = new HashSet<String>() {
      {
        add("notExistingEmailAddress@notExistingEmailAddress.com");
      }
    };
    when(
        messageSource.getMessage("email.auto.generated.notice", new Object[] {content},
            LocaleContextHolder.getLocale())).thenReturn("email content");
    when(properties.getProperty("email.content.template")).thenReturn("start %NOTICE% end");

    try {
      service.generateAndSendEmail(subject, content, recipients);
    } catch (MessagingException e) {
      fail();
    }
  }
}
