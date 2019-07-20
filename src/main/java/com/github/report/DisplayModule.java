package com.github.report;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.report.object.Branch;
import com.github.report.object.Repository;
import com.github.report.object.User;

public class DisplayModule {
  private static final Logger LOGGER = LogManager.getLogger();

  public void displayRepo(List<Repository> repos) {
    for (Repository repo : repos) {
      LOGGER.info("StaleBranchData Name : " + repo.getRepoName());
    }
  }

  public void displayUsers(List<User> users) {
    for (User user : users) {
      LOGGER.info("User Name : " + user.getLogin());
    }
  }

  public void displayBranch(Branch branch) {
    LOGGER.info("Branch Name : " + branch.getName());

    String logMessage = branch.getInactiveAge() + "---- branch Status : " + branch.isActive() + "----" + branch.getMostRecentCommit().getAuthor().getName() + "----"
        + branch.getMostRecentCommit().getDate() + "----" + branch.getMostRecentCommit().getAuthor().getEmail() + "----" + branch.getMostRecentCommit().getAuthor().getLogin();

    if (branch.getIssue() != null) {
      LOGGER.info("----" + branch.getIssue().getIssueKey() + "----" + branch.getIssue().getStatus());
    }
    LOGGER.info(logMessage);
  }
}
