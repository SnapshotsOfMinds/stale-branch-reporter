package com.github.report.rest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({ MockitoExtension.class })
public class QueryGitHubTest
{

    private GitHubDataRetriever testQueryGitHub;

    @BeforeEach
    public void setUp() throws Exception
    {
        testQueryGitHub = new GitHubDataRetriever();
    }

    /**
     * TODO Poorly written unit test as it actually performs the query and fails now that some branches were cleaned up.
     * 
     * @throws Exception
     */
    // @Test
    public void retreiveGitHubData_Success() throws Exception
    {

        String result = testQueryGitHub.retreiveGitHubData(
                "https://github.cerner.com/api/v3/repos/Installation-Solution/installation-environment-controller/branches");

        assertTrue(result.contains("[{\"name\":\"2062-ValidateDomain\",\"commit\":{"));
    }
}
