package com.github.report;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.report.model.Branch;
import com.github.report.model.StaleBranchData;
import com.github.report.object.User;

public class DisplayModule
{
    private static final Logger LOGGER = LogManager.getLogger();

    public void displayRepo(List<StaleBranchData> repoList)
    {
        for (StaleBranchData repo : repoList)
        {
            LOGGER.info("StaleBranchData Name : " + repo.getRepoName());
        }
    }

    public void displayUsers(List<User> userList)
    {
        for (User user : userList)
        {
            LOGGER.info("User Name : " + user.getLogin());
        }
    }

    public void displayBranch(Branch b)
    {
        LOGGER.info("Branch Name : " + b.getBranchName());

        if (b.getIssue() != null)
        {
            LOGGER.info(b.getTimeStamp() + "---- branch Status : " + b.isActive() + "----" + b.getUser().getName()
                    + "----" + b.getDate() + "----" + b.getUser().getEmail() + "----" + b.getUser().getLogin() + "----" + b.getIssue().getIssueKey() + "----" + b.getIssue().getStatus());
        }
        else
        {
            LOGGER.info(b.getTimeStamp() + "---- branch Status : " + b.isActive() + "----" + b.getUser().getName()
                    + "----" + b.getDate() + "----" + b.getUser().getEmail() + "----" + b.getUser().getLogin());
        }
        LOGGER.info("");
    }
}
