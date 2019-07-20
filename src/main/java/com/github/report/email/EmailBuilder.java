package com.github.report.email;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.report.exception.EmailException;
import com.github.report.object.Branch;
import com.github.report.object.Organization;
import com.github.report.object.Repository;
import com.github.report.tool.I18NMessageUtility;

/**
 * Class responsible for creating an Email message.
 */
public class EmailBuilder {
  private static final Logger LOGGER = LogManager.getLogger();
  private static final I18NMessageUtility I18N_MESSAGE_UTILITY = new I18NMessageUtility();

  private static final String EMAIL_ADDRESS = "svcIPDEVinstsolusr@cerner.com";

  private static final String CONTENT_TYPE = "Content-type";
  private static final String HTML_TYPE = "text/HTML";
  private static final String CONTENT_VALUE = HTML_TYPE + "; charset=UTF-8";
  private static final String FORMAT = "format";
  private static final String FORMAT_VALUE = "flowed";
  private static final String ENCODING = "Content-Transfer-Encoding";
  private static final String ENCODING_VALUE = "8bit";
  private static final String SUBJECT_CHARSET = "UTF-8";

  /**
   * Utility method to send simple HTML email
   *
   * @param session The mail {@link Session}.
   * @param mailingList The {@link Set} of email addresses to send to.
   * @param subject The subject of the message.
   * @param body The body of the message.
   * @param org The {@link Organization} sender belongs to.
   * @return The {@link MimeMessage} with the header populated.
   * @throws EmailException If unable to create the message with the header information.
   */
  public MimeMessage finalizeMessage(Session session, Set<String> mailingList, String subject, String body, Organization org) throws EmailException {
    LOGGER.info(I18N_MESSAGE_UTILITY.getMessage("CREATING_EMAIL"));
    try {
      MimeMessage msg = getMimeMessage(session);

      // set message headers
      msg.addHeader(CONTENT_TYPE, CONTENT_VALUE);
      msg.addHeader(FORMAT, FORMAT_VALUE);
      msg.addHeader(ENCODING, ENCODING_VALUE);

      msg.setFrom(new InternetAddress(EMAIL_ADDRESS, org.getOrgName()));

      msg.setReplyTo(InternetAddress.parse(EMAIL_ADDRESS, false));

      msg.setSubject(subject, SUBJECT_CHARSET);

      msg.setContent(body, HTML_TYPE);

      msg.setSentDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

      for (String mailID : mailingList) {
        msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(mailID, false));
      }

      return msg;
    } catch (MessagingException | UnsupportedEncodingException e) {
      throw new EmailException(I18N_MESSAGE_UTILITY.getMessage("EMAIL_HEADER_FAILURE"), e);
    }
  }

  /**
   * Create the body of the message.
   *
   * @param repos The {@link List} of {@link Repository Repositories}.
   * @param org The {@link Organization} for which the branches belong.
   * @param stalePeriod The time period used when checking for staleness.
   * @return A string representation of the message body in HTML format.
   */
  public String createBody(List<Repository> repos, Organization org, int stalePeriod) {
    // @formatter:off
        String body = MessageFormat.format(
                "<html>"
                    + "<h4>"
                        + "<img src={0} alt=\"\" height=\"25\" width=\"25\" /> &nbsp&nbsp {1} : {2}"
                    + "</h4>"
                    + "<h5>"
                        + "{3}&nbsp-&nbsp{4}&nbsp{5}"
                    + "</h5>"
                    + "<body>"
                        + "<div bgcolor=\"#666\">"
                            + "<table border=\"1\" bgcolor=\"#ffe5e5\" padding=\"6px\" style=\"border:black\">"
                                + "<thead>"
                                    + "<tr>"
                                        + "<th max-width=\"10px\">{6}</th>"
                                        + "<th>{7}</th>"
                                        + "<th>{8}</th>"
                                        + "<th>{9}</th>"
                                    + "</tr>"
                                + "</thead>",
                org.getOrgImage(), I18N_MESSAGE_UTILITY.getMessage("EMAIL_BODY_TITLE"), org.getOrgName(), I18N_MESSAGE_UTILITY.getMessage("EMAIL_CONTENT_DESCRIPTION"),
                stalePeriod, I18N_MESSAGE_UTILITY.getMessage("DAYS_LABEL"), I18N_MESSAGE_UTILITY.getMessage("BRANCH_NAME_COLUMN_HEADER"),
                I18N_MESSAGE_UTILITY.getMessage("AGE_COLUMN_HEADER"), I18N_MESSAGE_UTILITY.getMessage("AUTHOR_COLUMN_HEADER"),
                I18N_MESSAGE_UTILITY.getMessage("JIRA_STATUS_HEADER"));

    // @formatter:on
    StringBuilder mailBody = new StringBuilder(body);

    for (Repository repo : repos) {
      mailBody.append("<tr>");
      mailBody.append("<td colspan=\"4\"><b>").append(I18N_MESSAGE_UTILITY.getMessage("REPOSITORY_HEADER")).append(" : ").append("&nbsp" + repo.getRepoName() + "&nbsp").append("</b></td>");
      mailBody.append("</tr>");

      for (Branch branch : repo.getStaleBranches()) {
        mailBody.append("<tr>");
        // Branch Name with link
        mailBody.append("<td><a href =" + branch.getLink() + ">").append(branch.getName()).append("</a></td>");

        // Branch Age
        mailBody.append("<td>").append(branch.getInactiveAge()).append("</td>");

        // Branch avatar and author name
        mailBody
            .append(
                "<td align=\"left\"><img src=" + branch.getMostRecentCommit().getAuthor().getAvatar() + "alt=" + branch.getMostRecentCommit().getAuthor().getLogin() + "height=\"18\" width=\"18\" /> ")
            .append(branch.getMostRecentCommit().getAuthor().getLogin()).append("</td>");

        // Jira Status
        mailBody.append("<td>");
        if (branch.getIssue() != null) {
          if (branch.getIssue().getStatus().equalsIgnoreCase("Closed")) {
            mailBody.append("<font color=\"red\">").append(branch.getIssue().getStatus()).append("</font>");
          } else {
            mailBody.append(branch.getIssue().getStatus());
          }
        } else {
          mailBody.append("n/a");
        }

        mailBody.append("</td></tr>");
      }
    }

    mailBody.append("</table></div></body></html>");

    return mailBody.toString();
  }

  /**
   * Wrapper method to return a new {@link MimeMessage} object.
   *
   * @param session The mail {@link Session}.
   * @return The created MimeMessage object.
   */
  MimeMessage getMimeMessage(Session session) {
    return new MimeMessage(session);
  }
}
