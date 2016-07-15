package com.smartwg.core.internal.services.impl;

import com.smartwg.core.internal.services.EmailGatewayService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Kamil Sierzant (ks)
 */
@Named
@Scope(value = "singleton")
public class EmailGatewayServiceImpl implements EmailGatewayService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailGatewayServiceImpl.class);

  @Inject
  private MessageSource messageSource;
  @Inject
  private Properties properties;

  public void generateAndSendEmail(final String subject, final String content,
      final Set<String> recipients) throws MessagingException {
    final MimeMessage message = createMessage(subject, content, recipients);

    try {
      Transport.send(message);
      LOGGER.info("Email successfully sent.");
    } catch (MessagingException e) {
      LOGGER.error("Sending email failed!");
      throw new MessagingException();
    }
  }

  private MimeMessage createMessage(final String subject, final String content,
      final Set<String> recipients) {
    final Session session = getSession();

    final MimeMessage message = new MimeMessage(session);
    try {
      LOGGER.info("Preparing email to send.");
      message.addHeader("Content-type", "text/HTML; charset=UTF-8");
      message.addHeader("format", "flowed");
      message.addHeader("Content-Transfer-Encoding", "8bit");
      message.setFrom(new InternetAddress(properties.getProperty("mail.address")));
      for (String recipient : recipients) {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
      }
      message.setSubject(subject, "UTF-8");
      String notice =
          messageSource.getMessage("email.auto.generated.notice", new Object[] {content},
              LocaleContextHolder.getLocale());
      String property =
          properties.getProperty("email.content.template").replace("%NOTICE%", notice);
      String text = property.replace("%DATA%", content);
      message.setContent(text, "text/HTML; charset=UTF-8");
    } catch (MessagingException e) {
      LOGGER.error("Email could not be created!");
      e.printStackTrace();
    }
    return message;
  }

  private Session getSession() {
    final Properties mailServerProps = System.getProperties();
    mailServerProps.put("mail.smtp.host", properties.getProperty("mail.smtp.host"));
    mailServerProps.put("mail.smtp.socketFactory.port", properties.getProperty("mail.smtp.port"));
    mailServerProps.put("mail.smtp.socketFactory.class", properties.getProperty("mail.smtp.class"));
    mailServerProps.put("mail.smtp.auth", properties.getProperty("mail.smtp.auth"));
    mailServerProps.put("mail.smtp.port", properties.getProperty("mail.smtp.port"));

    return Session.getDefaultInstance(mailServerProps, new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(properties.getProperty("mail.address"), "smartyase");
      }
    });
  }
}
