package com.github.report.email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.report.exception.EmailException;
import com.github.report.tool.I18NMessageUtility;

/**
 * Utility to assist in creating and sending an email.
 */
public class EmailSender {
  private static final Logger LOGGER = LogManager.getLogger();
  private static final I18NMessageUtility I18N_MESSAGE_UTILITY = new I18NMessageUtility();

  /**
   * Utility method to send simple HTML email
   *
   * @param msg The {@link MimeMessage} to send.
   * @throws EmailException If unable to send the email.
   */
  public void sendEmail(Message msg) throws EmailException {
    try {
      LOGGER.info(I18N_MESSAGE_UTILITY.getMessage("SENDING_EMAIL"));

      send(msg);

      LOGGER.info(I18N_MESSAGE_UTILITY.getMessage("EMAIL_SENT_SUCCESSFULLY"));
    } catch (MessagingException e) {
      throw new EmailException(I18N_MESSAGE_UTILITY.getMessage("EMAIL_SEND_FAILURE"), e);
    }
  }

  /**
   * Wrapper method to send the message.
   *
   * @param msg The message to send.
   * @throws MessagingException If unable to send the message.
   */
  void send(Message msg) throws MessagingException {
    Transport.send(msg);
  }
}
