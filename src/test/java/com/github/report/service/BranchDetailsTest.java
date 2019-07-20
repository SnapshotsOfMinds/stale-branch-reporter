package com.github.report.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.github.report.json.parser.BranchJSONParser;
import com.github.report.object.Branch;

@ExtendWith({ MockitoExtension.class })
public class BranchDetailsTest
{
    @Spy
    private BranchJSONParser spyBranchDetails;

    @Mock
    private BranchService mockBranchService;

    @Test
    public void getBranchInfo_success() throws JSONException, IOException
    {
        String branchJSONdata = "[\r\n" +
                "  {\r\n" +
                "    \"name\": \"2062-ValidateDomain\",\r\n" +
                "    \"commit\": {\r\n" +
                "      \"sha\": \"4df1fc0a69e09b1d0c6632478a14611a02b9a928\",\r\n" +
                "      \"url\": \"https://github.cerner.com/api/v3/repos/Installation-Solution/installation-environment-controller/commits/4df1fc0a69e09b1d0c6632478a14611a02b9a928\"\r\n" +
                "    }\r\n" +
                "  },\r\n" +
                "  {\r\n" +
                "    \"name\": \"master\",\r\n" +
                "    \"commit\": {\r\n" +
                "      \"sha\": \"0b9717e9d7a857658e615557ed710f56779bf066\",\r\n" +
                "      \"url\": \"https://github.cerner.com/api/v3/repos/Installation-Solution/installation-environment-controller/commits/0b9717e9d7a857658e615557ed710f56779bf066\"\r\n" +
                "    }\r\n" +
                "  }\r\n" +
                " ]";

        String commitJSONData = "{\r\n" + "  \"sha\": \"4df1fc0a69e09b1d0c6632478a14611a02b9a928\",\r\n"
                + "  \"commit\": {\r\n" + "    \"author\": {\r\n" + "      \"name\": \"Brett Cooper\",\r\n"
                + "      \"email\": \"Brett.Cooper@cerner.com\",\r\n" + "      \"date\": \"2016-08-25T15:55:11Z\"\r\n"
                + "    },\r\n" + "    \"committer\": {\r\n" + "      \"name\": \"Brett Cooper\",\r\n"
                + "      \"email\": \"Brett.Cooper@cerner.com\",\r\n" + "      \"date\": \"2016-08-25T15:55:11Z\"\r\n"
                + "    }\r\n" + "	},\r\n"
                + "	\"html_url\": \"https://github.cerner.com/Installation-Solution/installation-environment-controller/commit/4df1fc0a69e09b1d0c6632478a14611a02b9a928\",\r\n"
                + "	\"author\": {\r\n" + "    \"login\": \"bc015394\",\r\n" + "    \"id\": 2435,\r\n"
                + "    \"avatar_url\": \"https://avatars.github.cerner.com/u/2435?\",\r\n"
                + "    \"gravatar_id\": \"\"\r\n" + "  }\r\n" + "}";

        int stalePeriod = 45;

        when(spyBranchDetails.getBranchService()).thenReturn(mockBranchService);
        when(mockBranchService.commitsURL(anyString())).thenReturn(commitJSONData);

        ArrayList<Branch> actuaList = spyBranchDetails.getBranchInfo(branchJSONdata, stalePeriod);
        assertEquals("Brett.Cooper@cerner.com", actuaList.get(0).getUser().getEmail());

        String commitJSONData_noAuthor = "{\r\n" + "  \"sha\": \"4df1fc0a69e09b1d0c6632478a14611a02b9a928\",\r\n"
                + "  \"commit\": {\r\n" + "    \"author\": {\r\n" + "      \"name\": \"Brett Cooper\",\r\n"
                + "      \"email\": \"Brett.Cooper@cerner.com\",\r\n" + "      \"date\": \"2016-08-25T15:55:11Z\"\r\n"
                + "    },\r\n" + "    \"committer\": {\r\n" + "      \"name\": \"Brett Cooper\",\r\n"
                + "      \"email\": \"Brett.Cooper@cerner.com\",\r\n" + "      \"date\": \"2016-08-25T15:55:11Z\"\r\n"
                + "    }\r\n" + "	},\r\n"
                + "	\"html_url\": \"https://github.cerner.com/Installation-Solution/installation-environment-controller/commit/4df1fc0a69e09b1d0c6632478a14611a02b9a928\",\r\n"
                + "	\"committer\": {\r\n" + "    \"login\": \"bc015394\",\r\n" + "    \"id\": 2435,\r\n"
                + "    \"avatar_url\": \"https://avatars.github.cerner.com/u/2435?\"\r\n" + "  }\r\n" + "}";

        when(mockBranchService.commitsURL(anyString())).thenReturn(commitJSONData_noAuthor);
        actuaList = spyBranchDetails.getBranchInfo(branchJSONdata, stalePeriod);
        assertEquals("Brett.Cooper@cerner.com", actuaList.get(0).getUser().getEmail());

        String commitJSONData_invalidUser = "{\r\n" + "  \"sha\": \"4df1fc0a69e09b1d0c6632478a14611a02b9a928\",\r\n"
                + "  \"commit\": {\r\n" + "    \"author\": {\r\n" + "      \"name\": \"Brett Cooper\",\r\n"
                + "      \"email\": \"Brett.Cooper@cerner.com\",\r\n" + "      \"date\": \"2018-12-03T15:55:11Z\"\r\n"
                + "    },\r\n" + "    \"committer\": {\r\n" + "      \"name\": \"Brett Cooper\",\r\n"
                + "      \"email\": \"Brett.Cooper@cerner.com\",\r\n" + "      \"date\": \"2018-12-03T15:55:11Z\"\r\n"
                + "    }\r\n" + "	},\r\n"
                + "	\"html_url\": \"https://github.cerner.com/Installation-Solution/installation-environment-controller/commit/4df1fc0a69e09b1d0c6632478a14611a02b9a928\"	\r\n"
                + "}";

        when(mockBranchService.commitsURL(anyString())).thenReturn(commitJSONData_invalidUser);
        actuaList = spyBranchDetails.getBranchInfo(branchJSONdata, stalePeriod);
        assertEquals("InvalidUser", actuaList.get(0).getUser().getLogin());
    }
}
