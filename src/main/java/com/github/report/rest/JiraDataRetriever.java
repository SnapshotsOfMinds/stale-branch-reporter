package com.github.report.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.report.exception.RestServiceException;
import com.github.report.tool.I18NMessageUtility;

/**
 * Class responsible for retrieving appropriate data from Jira.
 */
public class JiraDataRetriever extends DataRetriever
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final I18NMessageUtility I18N_MESSAGE_UTILITY = new I18NMessageUtility();

    private static final String JIRA_REST_URL = "https://jira2.cerner.com/rest/api/2/";

    /**
     * Retrieve the JSON representation of the reply by executing JQL query using issue key.
     *
     * @param issueKeys The {@link List} of Jira issue keys.
     * @return The JSON representation of the reply from Jira.
     * @throws RestServiceException If unable to create the URL or parse the URL response.
     */
    public String retrieveJiraByIssueKey(List<String> issueKeys) throws RestServiceException
    {
        LOGGER.info(I18N_MESSAGE_UTILITY.getMessage("RETRIEVING_STATUS_DETAILS", issueKeys.size(), String.join(", ", issueKeys)));

        URL url = getURL(JIRA_REST_URL + "search?jql=issueKey%20in%20(" + String.join(",", issueKeys) + ")");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getURLConnection(url).getInputStream())))
        {
            return reader.lines().collect(Collectors.joining("\n"));
        }
        catch (IOException e)
        {
            throw new RestServiceException(I18N_MESSAGE_UTILITY.getMessage("FAILURE_RETRIEVING_JIRA_DATA"), e);
        }
    }
}
