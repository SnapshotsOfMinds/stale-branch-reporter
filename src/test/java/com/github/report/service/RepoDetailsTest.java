package com.github.report.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.report.model.StaleBranchData;

@ExtendWith({ MockitoExtension.class })
public class RepoDetailsTest
{
    @Spy
    private RepoDetails spyRepoDetails;

    @Test
    public void getRepoInfo_Succes()
    {

        String RepoJson = "[\r\n" + "  {\r\n" + "    \"id\": 57853,\r\n"
                + "    \"name\": \"centralized-schema-deployment\"\r\n" + "   },\r\n" + "   {\r\n"
                + "    \"id\": 57855,\r\n" + "    \"name\": \"ui-console\"\r\n" + "   }\r\n" + " ]";

        ArrayList<StaleBranchData> expected_repo_list = new ArrayList<>();
        StaleBranchData repo1 = new StaleBranchData();
        repo1.setRepoName("centralized-schema-deployment");
        StaleBranchData repo2 = new StaleBranchData();
        repo2.setRepoName("ui-console");

        expected_repo_list.add(repo1);
        expected_repo_list.add(repo2);

        assertEquals(expected_repo_list.get(0).getRepoName(),
                spyRepoDetails.getRepoInfo(RepoJson).get(0).getRepoName());
    }
}
