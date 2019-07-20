package com.github.report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.report.model.Branch;
import com.github.report.model.Issue;
import com.github.report.model.StaleBranchData;
import com.github.report.object.Organization;
import com.github.report.rest.GitHubDataRetriever;
import com.github.report.rest.JiraDataRetriever;

public class StaleBranches
{
    private static final String PROJECT_NAME = "INSTALLSOL";
    private static Set<String> mailingList = new HashSet<>();

    /**
     * @param repoList
     * @param org
     * @param stalePeriod
     * @return
     * @throws Exception
     */
    public List<StaleBranchData> retrieveStaleBranches(List<StaleBranchData> repoList, Organization org, int stalePeriod) throws Exception
    {
        ArrayList<StaleBranchData> staleBranchItems = new ArrayList<>();
        JiraService jiraService = new JiraService();

        for (StaleBranchData staleBranchDataPerRepo : repoList)
        {
            ArrayList<Branch> staleBranchList = new ArrayList<>();
            String branchData = getGitHubDataRetriever().retrieveBranches(staleBranchDataPerRepo.getRepoName(), org);
            List<Branch> BranchList = getBranchDetails().getBranchInfo(branchData, stalePeriod);

            Set<String> emailIDs = getBranchDetails().getEmailIDs();
            mailingList.addAll(emailIDs);

            String jiraNumber = "";
            String issueKey;
            String jiraResponse;
            Map<String, String> jiraStatusOnIssuekey = new HashMap<>();

            // Gather Jira info from Jira service
            List<String> jiraNumbers = BranchList.stream()
                    .filter(b -> !b.isActive())
                    .map(b -> b.getBranchName().replaceAll("[^0-9]", ""))
                    .filter(b -> b.length() > 0)
                    .collect(Collectors.toList());

            System.out.println("jiraNumbers size:" + jiraNumbers.size());
            List<String> issueKeys = jiraNumbers.stream().map(b -> PROJECT_NAME + "-" + b).collect(Collectors.toList());
            System.out.println("issueKeys size:" + issueKeys.size());

            if (issueKeys.size() > 0)
            {
                jiraResponse = new JiraDataRetriever().retrieveJiraByIssueKey(issueKeys);
                jiraStatusOnIssuekey = jiraService.getJiraStatus(jiraResponse);
            }

            for (Branch b : BranchList)
            {
                // Branch is not a active one
                if (!b.isActive())
                {
                    jiraNumber = b.getBranchName().replaceAll("[^0-9]", "");
                    if (!jiraNumber.isEmpty())
                    {
                        issueKey = PROJECT_NAME + "-" + jiraNumber;

                        if (jiraStatusOnIssuekey.containsKey(issueKey))
                        {
                            System.out.println("Jira status for " + issueKey + " is " + jiraStatusOnIssuekey.get(issueKey));
                            Issue issue = new Issue();
                            issue.setIssueKey(issueKey);
                            issue.setStatus(jiraStatusOnIssuekey.get(issueKey));
                            b.setIssue(issue);
                        }
                    }
                    if (b.getIssue() == null ||
                            (b.getIssue() != null
                                    && !(b.getIssue().getStatus().equalsIgnoreCase("open")
                                            || b.getIssue().getStatus().equalsIgnoreCase("in progress"))))
                    {
                        staleBranchList.add(b);
                    }
                }
            }

            if (!staleBranchList.isEmpty())
            {
                staleBranchDataPerRepo.setStaleBranchList(staleBranchList);
                staleBranchItems.add(staleBranchDataPerRepo);
            }
        }
        return staleBranchItems;
    }

    public Set<String> getMailingList()
    {
        return mailingList;
    }

    public GitHubDataRetriever getGitHubDataRetriever()
    {
        return new GitHubDataRetriever();
    }

    public BranchDetails getBranchDetails()
    {
        return new BranchDetails();
    }
}
