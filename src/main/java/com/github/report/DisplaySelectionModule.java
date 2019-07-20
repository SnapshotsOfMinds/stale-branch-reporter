package com.github.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.report.email.EmailBuilder;
import com.github.report.json.parser.JSONParser;
import com.github.report.json.parser.UserJSONParser;
import com.github.report.model.Branch;
import com.github.report.model.StaleBranchData;
import com.github.report.object.Organization;
import com.github.report.object.User;
import com.github.report.rest.GitHubDataRetriever;
import com.github.report.service.BranchDetails;
import com.github.report.service.RepoDetails;
import com.github.report.service.StaleBranches;

public class DisplaySelectionModule
{
    private static final Logger LOGGER = LogManager.getLogger();
    private Scanner sc = new Scanner(System.in);

    public void displayBranchDetails(List<StaleBranchData> repoList, Organization org, String author, int stalePeriod)
    {

        System.out.println("Listing All Stale Branches ");

        try
        {
            for (StaleBranchData repo : repoList)
            {

                String branchData = getGitHubDataRetriever().retrieveBranches(repo.getRepoName(), org);
                List<Branch> branchList = getBranchDetails().getBranchInfo(branchData, stalePeriod);

                if (!branchList.isEmpty())
                {

                    LOGGER.info("----------------------Showing Branches for StaleBranchData :" + repo.getRepoName() + "----------------");

                    for (Branch b : branchList)
                    {

                        if (author == null)
                        {
                            if (!b.isActive())
                            {
                                getDisplayModule().displayBranch(b);
                            }
                        }
                        else if (!b.isActive() && author.equals(b.getUser().getLogin()))
                        {

                            getDisplayModule().displayBranch(b);
                        }
                    }
                }
                LOGGER.info("------------------------------------------------------------------------------------");
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public List<StaleBranchData> displayRepoList(Organization org)
    {

        List<StaleBranchData> repoList = new ArrayList<>();

        try
        {

            LOGGER.info("List All Projects(Repos)");
            String repoData = getGitHubDataRetriever().retrieveRepos(org);
            repoList = getRepoDetails().getRepoInfo(repoData);
            getDisplayModule().displayRepo(repoList);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return repoList;
    }

    /**
     * Display stale branches
     *
     * @param repoList
     * @param org
     * @param stalePeriod
     * @return
     */
    public String displayStaleBranches(List<StaleBranchData> repoList, Organization org, int stalePeriod)
    {
        try
        {
            System.out.println(
                    "***************************************From Main - For Mailing******************************************");

            List<StaleBranchData> repoStaleBranchList = getStaleBranches().retrieveStaleBranches(repoList, org, stalePeriod);

            if (repoStaleBranchList.isEmpty())
            {
                return null;
            }

            return getEmailBuilder().createBody(repoStaleBranchList, org, stalePeriod);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void displayUsersList(List<StaleBranchData> repoList, Organization org)
    {

        try
        {
            for (StaleBranchData repo : repoList)
            {
                LOGGER.info("*************** Contributors to StaleBranchData : " + repo.getRepoName() + "************");
                String userData = getGitHubDataRetriever().retrieveUsers(repo.getRepoName(), org);
                List<User> userList = getUserJSONParser().parse(userData);
                getDisplayModule().displayUsers(userList);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public GitHubDataRetriever getGitHubDataRetriever()
    {
        return new GitHubDataRetriever();
    }

    public BranchDetails getBranchDetails()
    {
        return new BranchDetails();
    }

    public JSONParser<User> getUserJSONParser()
    {
        return new UserJSONParser();
    }

    public DisplayModule getDisplayModule()
    {
        return new DisplayModule();
    }

    public RepoDetails getRepoDetails()
    {
        return new RepoDetails();
    }

    public StaleBranches getStaleBranches()
    {
        return new StaleBranches();
    }

    public EmailBuilder getEmailBuilder()
    {
        return new EmailBuilder();
    }
}
