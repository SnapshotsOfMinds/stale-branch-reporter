package com.github.report.model;

import java.util.ArrayList;

/**
 *
 */
public class StaleBranchData
{
    private String repoName;
    private ArrayList<Branch> staleBranchList;

    public String getRepoName()
    {
        return repoName;
    }

    public void setRepoName(String repoName)
    {
        this.repoName = repoName;
    }

    public ArrayList<Branch> getStaleBranchList()
    {
        return staleBranchList;
    }

    public void setStaleBranchList(ArrayList<Branch> staleBranchList)
    {
        this.staleBranchList = staleBranchList;
    }
}
