package com.github.report.model;

import com.github.report.object.User;

/**
 *
 */
public class Branch
{
    private String date;
    private String branchName;
    private User user;
    private boolean isActive;
    private String timeStamp;
    private String branchLink;
    private Issue issue;

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setIsActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public String getBranchLink()
    {
        return branchLink;
    }

    public void setBranchLink(String branchLink)
    {
        this.branchLink = branchLink;
    }

    public Issue getIssue()
    {
        return issue;
    }

    public void setIssue(Issue issue)
    {
        this.issue = issue;
    }
}
