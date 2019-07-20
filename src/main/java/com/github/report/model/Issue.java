package com.github.report.model;

/**
 * Represents Jira Issue.
 */
public class Issue
{
    String issueKey;
    String status;

    public String getIssueKey()
    {
        return issueKey;
    }

    public void setIssueKey(String issueKey)
    {
        this.issueKey = issueKey;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
