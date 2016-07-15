package com.smartwg.core.internal.services;

import org.springframework.scheduling.annotation.Async;

import java.util.Set;

import javax.mail.MessagingException;

/**
 * @author Kamil Sierzant (ks)
 */
public interface EmailGatewayService {

  /**
   * Generates and send an email
   *
   * @param subject {@link String} is the subject of email
   * @param content {@link String} is the content of email
   * @param recipients {@link Set} of recipient emails
   * @throws MessagingException {@link MessagingException} is throws if sending email fails
   */
  @Async
  void generateAndSendEmail(String subject, String content, Set<String> recipients)
      throws MessagingException;
}
