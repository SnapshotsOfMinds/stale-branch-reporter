package com.github.report.command;

import java.io.IOException;
import java.util.List;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.github.report.CommandMode;
import com.github.report.exception.CommandException;
import com.github.report.exception.JSONParseException;
import com.github.report.exception.RestServiceException;
import com.github.report.object.Organization;
import com.github.report.object.Repository;

/**
 *
 */
@Parameters(commandDescription = "Displays the repository information for the given organization.", separators = "=")
public class RepositoryCommand extends Command {
  @Parameter(names = {"-o", "--organization"}, required = true, order = 0, description = "Team or Organization Name as displayed in GitHub")
  private String orgName;

  @Parameter(names = {"-b", "--branches"}, required = false, order = 1, description = "Flag to determine if the branches for the repositories should be included.")
  private boolean includeBranches;

  @Override
  public boolean execute() throws CommandException {
    try {
      if (displayHelp()) {
        super.displayHelpInformation(CommandMode.REPOSITORIES);
        return true;
      }

      Organization org = getGitHubDataRetriever().retrieveOrgDetails(orgName);

      List<Repository> repos = getRepoJSONParser().parse(getGitHubDataRetriever().retrieveRepos(org));

      if (includeBranches) {
        for (Repository repo : repos) {
          String branchData = getGitHubDataRetriever().retrieveBranches(repo.getRepoName(), org);
          repo.setBranches(getBranchJSONParser(Long.MAX_VALUE).parse(branchData));
        }
      }

      repos.forEach(System.out::println);
    } catch (RestServiceException | JSONParseException | IOException e) {
      throw new CommandException(e);
    }

    return true;
  }
}
