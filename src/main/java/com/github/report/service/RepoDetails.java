package com.github.report.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.report.model.StaleBranchData;

/**
 *
 */
public class RepoDetails
{
    private ArrayList<StaleBranchData> repoList = new ArrayList<>();

    /**
     * @param repoJSONData
     * @return List Of StaleBranchData Names
     * @throws JSONException
     */
    public List<StaleBranchData> getRepoInfo(String repoJSONData) throws JSONException
    {

        JSONArray jsonArray = new JSONArray(repoJSONData);
        for (int i = 0; i < jsonArray.length(); i++)
        {
            StaleBranchData repo = new StaleBranchData();
            JSONObject jsonStatusObject = jsonArray.getJSONObject(i);
            repo.setRepoName(jsonStatusObject.getString("name"));
            repoList.add(repo);
        }
        return repoList;
    }
}
