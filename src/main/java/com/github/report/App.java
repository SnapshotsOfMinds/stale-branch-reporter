package com.github.report;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.github.report.email.EmailBuilder;
import com.github.report.email.EmailSender;
import com.github.report.exception.EmailException;
import com.github.report.object.Organization;
import com.github.report.object.Repository;
import com.github.report.rest.GitHubDataRetriever;
import com.github.report.service.StaleBranches;

/**
 * Stale Branch Reporter Main class
 */
public class App
{
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String SMTP_HOST_SERVER = "smtplb.cerner.com";
    private static final String SMTP_HOST_PROPERTY = "mail.smtp.host";

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        final MainCLIParams mainArguments = new MainCLIParams();

        App application = new App();
        JCommander jCommander = new JCommander(mainArguments);
        jCommander.setProgramName("GitStaleBranchReport");

        try
        {
            jCommander.parse(args);
        }
        catch (ParameterException exception)
        {
            System.out.println(exception.getMessage());
            showUsage(jCommander);
        }

        if (mainArguments.getOrg().isEmpty())
        {
            showUsage(jCommander);
        }

        application.run(mainArguments);

        if (mainArguments.isHelp())
        {
            showUsage(jCommander);
        }
    }

    static void showUsage(JCommander jCommander)
    {
        jCommander.usage();
        System.exit(0);
    }

    /**
     * @param mainArguments
     */
    public void run(MainCLIParams mainArguments)
    {
        try
        {
            Organization org = getGitHubDataRetriever().retrieveOrgDetails(mainArguments.getOrg());
            LOGGER.info("Working on Org " + mainArguments.getOrg());

            int stalePeriod = mainArguments.getStalePeriod();

            // Set default stale period
            if (stalePeriod == 0)
            {
                stalePeriod = 30;
            }

            List<Repository> repoList = getDisplaySelectionModule().displayRepoList(org);

            String mailBody = getDisplaySelectionModule().displayStaleBranches(repoList, org, stalePeriod);

            if (mailBody != null)
            {

                Set<String> emailRecipients = mainArguments.getEmailRecipients();

                getEmailSender().sendEmail(getMessage(emailRecipients.isEmpty() ? getStaleBranches().getMailingList() : emailRecipients, mailBody, org));
            }
            System.out.println("________________________________________________________________________________");

            getDisplaySelectionModule().displayUsersList(repoList, org);
            getDisplaySelectionModule().displayBranchDetails(repoList, org, mainArguments.getAuthor(), stalePeriod);

            System.out.println("------------- Over ---------------");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    EmailSender getEmailSender()
    {
        return new EmailSender();
    }

    EmailBuilder getEmailBuilder()
    {
        return new EmailBuilder();
    }

    GitHubDataRetriever getGitHubDataRetriever()
    {
        return new GitHubDataRetriever();
    }

    StaleBranches getStaleBranches()
    {
        return new StaleBranches();
    }

    DisplaySelectionModule getDisplaySelectionModule()
    {
        return new DisplaySelectionModule();
    }

    private Message getMessage(Set<String> emailRecipients, String body, Organization org) throws EmailException
    {
        Properties props = System.getProperties();
        props.put(SMTP_HOST_PROPERTY, SMTP_HOST_SERVER);

        return getEmailBuilder().finalizeMessage(Session.getInstance(props, null), emailRecipients, "Stale Branch Details", body, org);
    }
}
