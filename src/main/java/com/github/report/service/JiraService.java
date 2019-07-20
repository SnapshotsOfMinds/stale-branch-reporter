package com.github.report.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 */
public class JiraService
{
    /**
     * Returns Jira Status
     *
     * @param jiraResponse
     * @return
     */
    public Map<String, String> getJiraStatus(String jiraResponse)
    {
        Map<String, String> jiraStatusOnIssuekey = new HashMap<>();

        JSONObject jsonObject = new JSONObject(jiraResponse);
        JSONArray issues = jsonObject.getJSONArray("issues");

        String jiraKey;
        String jiraStatus;
        for (int i = 0; i < issues.length(); i++)
        {
            JSONObject issueItem = issues.getJSONObject(i);

            jiraKey = issueItem.getString("key");

            JSONObject jsonStatusObject = issueItem.getJSONObject("fields").getJSONObject("status");

            jiraStatus = jsonStatusObject.getString("name");

            jiraStatusOnIssuekey.put(jiraKey, jiraStatus);
        }

        return jiraStatusOnIssuekey;
    }
}
