package com.github.report.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.github.report.CommandMode;
import com.github.report.exception.CommandException;
import com.github.report.exception.JSONParseException;
import com.github.report.exception.RestServiceException;
import com.github.report.object.Organization;
import com.github.report.object.Repository;
import com.github.report.object.User;

/**
 * Display {@link User} information for all the {@link Repository Repositories}. Optionally,
 * specific repositories can be filtered down. Also, able to group the users by the repositories.
 */
@Parameters(commandDescription = "Displays the user information for the given organization's repositories.", separators = "=")
public class UserCommand extends Command {
  @Parameter(names = {"-o", "--organization"}, required = true, order = 0, description = "Team or Organization Name as displayed in GitHub.")
  private String orgName;

  @Parameter(names = {"-r", "--repositories"}, required = false, order = 1, description = "The repositories to retrieve the users for.")
  private List<String> repoNames = new ArrayList<>();

  @Parameter(names = {"-g", "--group-by"}, required = false, order = 2, description = "Group the users by the repositories.")
  private boolean groupByRepo = false;

  @Override
  public boolean execute() throws CommandException {
    try {
      if (displayHelp()) {
        super.displayHelpInformation(CommandMode.USERS);
        return true;
      }

      Organization org = getGitHubDataRetriever().retrieveOrgDetails(orgName);

      List<Repository> repos = getRepoJSONParser().parse(getGitHubDataRetriever().retrieveRepos(org));

      if (!repoNames.isEmpty()) {
        repos = repos.stream().filter(repo -> repoNames.contains(repo.getRepoName())).collect(Collectors.toList());
      }

      if (groupByRepo) {
        Map<String, String> users = new HashMap<>();
        for (Repository repo : repos) {
          users.put(repo.getRepoName(), getUserJSONParser().parse(getGitHubDataRetriever().retrieveUsers(repo.getRepoName(), org)).stream().map(User::toString).collect(Collectors.joining("\n\t")));
        }

        for (Map.Entry<String, String> entry : users.entrySet()) {
          System.out.println("Repository: " + entry.getKey());
          System.out.println("Users: \n\t" + entry.getValue());
        }
        return true;
      }

      Set<User> users = new HashSet<>();
      for (Repository repo : repos) {
        users.addAll(getUserJSONParser().parse(getGitHubDataRetriever().retrieveUsers(repo.getRepoName(), org)));
      }

      users.forEach(System.out::println);
    } catch (RestServiceException | JSONParseException | IOException e) {
      throw new CommandException(e);
    }

    return true;
  }
}
