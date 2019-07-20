package com.github.report.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import com.github.report.exception.RestServiceException;
import com.github.report.object.Organization;
import com.github.report.tool.I18NMessageUtility;

/**
 * Class responsible for retrieving appropriate data from GitHub.
 */
public class GitHubDataRetriever extends DataRetriever {
  private static final Logger LOGGER = LogManager.getLogger();
  private static final I18NMessageUtility I18N_MESSAGE_UTILITY = new I18NMessageUtility();

  private static final String USERS_API = "https://github.cerner.com/api/v3/users/";
  private static final String REPOS_API = "https://github.cerner.com/api/v3/repos/";

  private static final String AVATAR_URL = "avatar_url";

  private static final String URI_SEPARATOR = "/";
  private static final String REPOS = "repos";
  private static final String CONTRIBUTORS = "contributors";
  private static final String BRANCHES = "branches";

  /**
   * Retrieve the data from GitHub based off the provided URL.
   *
   * @param urlAddress The URL address to retrieve the necessary content.
   * @return The string representation of the GitHub content.
   * @throws RestServiceException If unable to create the URL or parse the URL response.
   */
  private String retreiveGitHubData(String urlAddress) throws RestServiceException {
    URL url = getURL(urlAddress);

    LOGGER.info(I18N_MESSAGE_UTILITY.getMessage("QUERYING_URL", urlAddress));

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(getURLConnection(url).getInputStream()))) {
      return reader.lines().collect(Collectors.joining("\n"));
    } catch (IOException e) {
      throw new RestServiceException(I18N_MESSAGE_UTILITY.getMessage("FAILURE_RETRIEVING_GITHUB_DATA"), e);
    }
  }

  /**
   * Retrieve the JSON string representation of the repository information from GitHub.
   *
   * @param org The {@link Organization} to retrieve the information for.
   * @return The JSON string representation of the repository information.
   * @throws RestServiceException If unable to retrieve the information.
   */
  public String retrieveRepos(Organization org) throws RestServiceException {
    return retreiveGitHubData(USERS_API + org.getOrgName() + URI_SEPARATOR + REPOS);
  }

  /**
   * Retrieve the {@link Organization} details from GitHub.
   *
   * @param orgName The name of the Organization to look up.
   * @return An Organization object containing the name and avatar link.
   * @throws RestServiceException If unable to retrieve the information.
   */
  public Organization retrieveOrgDetails(String orgName) throws RestServiceException {
    return new Organization(orgName, new JSONObject(retreiveGitHubData(USERS_API + orgName)).getString(AVATAR_URL));
  }

  /**
   * Retrieve the users for the provided project and {@link Organization} from GitHub.
   *
   * @param projectName The name of the project to retrieve the users for.
   * @param org The Organization to retrieve the information for.
   * @return The JSON string representation of the user data.
   * @throws RestServiceException If unable to retrieve the information.
   */
  public String retrieveUsers(String projectName, Organization org) throws RestServiceException {
    return retreiveGitHubData(REPOS_API + org.getOrgName() + URI_SEPARATOR + projectName + URI_SEPARATOR + CONTRIBUTORS);
  }

  /**
   * Retrieve the branch information from GitHub for all the project branches.
   *
   * @param projectName The name of the project to retrieve the users for.
   * @param org The {@link Organization} to retrieve the information for.
   * @return The JSON string representation of the branch data.
   * @throws RestServiceException If unable to retrieve the information.
   */
  public String retrieveBranches(String projectName, Organization org) throws RestServiceException {
    return retreiveGitHubData(REPOS_API + org.getOrgName() + URI_SEPARATOR + projectName + URI_SEPARATOR + BRANCHES);
  }

  /**
   * Retrieve the commit details as a JSON string based off the provided commit URL.
   *
   * @param commitURL The URL of the commit to obtain the details for.
   * @return The JSON representation of the commit details.
   * @throws RestServiceException If unable to retrieve the information.
   */
  public String retrieveCommits(String commitURL) throws RestServiceException {
    return retreiveGitHubData(commitURL);
  }
}
