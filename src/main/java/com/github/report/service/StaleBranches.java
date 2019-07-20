package com.github.report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.github.report.json.parser.BranchJSONParser;
import com.github.report.object.Branch;
import com.github.report.object.JiraStory;
import com.github.report.object.Organization;
import com.github.report.object.Repository;
import com.github.report.rest.GitHubDataRetriever;
import com.github.report.rest.JiraDataRetriever;

public class StaleBranches {
  private static final String PROJECT_NAME = "INSTALLSOL";
  private static Set<String> mailingList = new HashSet<>();

  /**
   * @param repoList
   * @param org
   * @param stalePeriod
   * @return
   * @throws Exception
   */
  public List<Repository> retrieveStaleBranches(List<Repository> repoList, Organization org, long stalePeriod) throws Exception {
    ArrayList<Repository> staleBranchItems = new ArrayList<>();
    JiraService jiraService = new JiraService();

    for (Repository staleBranchDataPerRepo : repoList) {
      List<Branch> staleBranchList = new ArrayList<>();
      String branchData = getGitHubDataRetriever().retrieveBranches(staleBranchDataPerRepo.getRepoName(), org);
      List<Branch> branches = getBranchJSONParser(stalePeriod).parse(branchData);

      mailingList.addAll(branches.stream().map(branch -> branch.getMostRecentCommit()).map(commit -> commit.getAuthor()).map(user -> user.getEmail()).collect(Collectors.toList()));

      String jiraNumber = "";
      String issueKey;
      String jiraResponse;
      Map<String, String> jiraStatusOnIssuekey = new HashMap<>();

      // Gather Jira info from Jira service
      List<String> jiraNumbers = branches.stream().filter(b -> !b.isActive()).map(b -> b.getName().replaceAll("[^0-9]", "")).filter(b -> b.length() > 0).collect(Collectors.toList());

      System.out.println("jiraNumbers size:" + jiraNumbers.size());
      List<String> issueKeys = jiraNumbers.stream().map(b -> PROJECT_NAME + "-" + b).collect(Collectors.toList());
      System.out.println("issueKeys size:" + issueKeys.size());

      if (issueKeys.size() > 0) {
        jiraResponse = new JiraDataRetriever().retrieveJiraByIssueKey(issueKeys);
        jiraStatusOnIssuekey = jiraService.getJiraStatus(jiraResponse);
      }

      for (Branch branch : branches) {
        // Branch is not a active one
        if (!branch.isActive()) {
          jiraNumber = branch.getName().replaceAll("[^0-9]", "");
          if (!jiraNumber.isEmpty()) {
            issueKey = PROJECT_NAME + "-" + jiraNumber;

            if (jiraStatusOnIssuekey.containsKey(issueKey)) {
              System.out.println("Jira status for " + issueKey + " is " + jiraStatusOnIssuekey.get(issueKey));
              JiraStory issue = new JiraStory();
              issue.setIssueKey(issueKey);
              issue.setStatus(jiraStatusOnIssuekey.get(issueKey));
              branch.setIssue(issue);
            }
          }
          if (branch.getIssue() == null || (branch.getIssue() != null && !(branch.getIssue().getStatus().equalsIgnoreCase("open") || branch.getIssue().getStatus().equalsIgnoreCase("in progress")))) {
            staleBranchList.add(branch);
          }
        }
      }

      if (!staleBranchList.isEmpty()) {
        staleBranchDataPerRepo.setStaleBranches(staleBranchList);
        staleBranchItems.add(staleBranchDataPerRepo);
      }
    }
    return staleBranchItems;
  }

  public Set<String> getMailingList() {
    return mailingList;
  }

  public GitHubDataRetriever getGitHubDataRetriever() {
    return new GitHubDataRetriever();
  }

  public BranchJSONParser getBranchJSONParser(long stalePeriod) {
    return new BranchJSONParser(stalePeriod);
  }
}
