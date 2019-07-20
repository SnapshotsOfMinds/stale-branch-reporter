package com.github.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.report.email.MailContent;
import com.github.report.json.parser.BranchJSONParser;
import com.github.report.json.parser.RepoJSONParser;
import com.github.report.json.parser.UserJSONParser;
import com.github.report.object.Branch;
import com.github.report.object.Organization;
import com.github.report.object.Repository;
import com.github.report.object.User;
import com.github.report.service.BranchService;
import com.github.report.service.RepoService;
import com.github.report.service.StaleBranches;
import com.github.report.service.UserService;

@ExtendWith({ MockitoExtension.class })
public class DisplaySeletionModuleTest
{
    @Mock
    private BranchService mockBranchService;
    @Mock
    private BranchJSONParser mockBranchDetails;
    @Mock
    private Organization mockOrg;
    @Mock
    private DisplayModule mockDisplayModule;
    @Mock
    private RepoService mockRepoService;
    @Mock
    private RepoJSONParser mockRepoDetails;
    @Mock
    private UserService mockUserService;
    @Mock
    private UserJSONParser mockUserDetails;
    @Mock
    private StaleBranches mockStaleBranches;
    @Mock
    private MailContent mockMailContent;

    @Spy
    private DisplaySelectionModule spyDisplaySeletionModule;

    @Test
    public void displayBranchDetails_Success() throws Exception
    {
        String mockAuthor = "bc015394";
        int mockStalePeriod = 30;
        String mockBranchData = "Some JSON data as String";

        User mockUser1 = new User();
        mockUser1.setLogin("bc015394");

        User mockUser2 = new User();
        mockUser2.setLogin("OtherUser");

        Branch mockStaleBranch = new Branch();
        mockStaleBranch.setBranchName("BugFix_Solution");
        mockStaleBranch.setIsActive(false);
        mockStaleBranch.setUser(mockUser1);

        Branch mockActiveBranch = new Branch();
        mockActiveBranch.setBranchName("Calculate_Age");
        mockActiveBranch.setIsActive(true);
        mockActiveBranch.setUser(mockUser2);

        Branch mockStaleBranch2 = new Branch();
        mockStaleBranch2.setBranchName("BugFix_Solutions");
        mockStaleBranch2.setIsActive(false);
        mockStaleBranch2.setUser(mockUser1);

        ArrayList<Branch> mockStaleBranchList = new ArrayList<>();
        mockStaleBranchList.add(mockStaleBranch);
        mockStaleBranchList.add(mockStaleBranch2);

        Repository repo1 = new Repository();
        repo1.setRepoName("business_Logic_Module");
        repo1.setStaleBranchList(mockStaleBranchList);

        ArrayList<Repository> mockRepoList = new ArrayList<>();
        mockRepoList.add(repo1);

        ArrayList<Branch> mockBranchList = new ArrayList<>();
        mockBranchList.add(mockActiveBranch);
        mockBranchList.add(mockStaleBranch);
        mockBranchList.add(mockStaleBranch2);

        when(spyDisplaySeletionModule.getBranchService()).thenReturn(mockBranchService);
        when(mockBranchService.getRepoBranches(anyString(), any(Organization.class))).thenReturn(mockBranchData);
        when(spyDisplaySeletionModule.getBranchDetails()).thenReturn(mockBranchDetails);
        when(mockBranchDetails.getBranchInfo(anyString(), anyInt())).thenReturn(mockStaleBranchList);

        when(spyDisplaySeletionModule.getDisplayModule()).thenReturn(mockDisplayModule);

        doNothing().when(mockDisplayModule).displayBranch(any(Branch.class));
        String Author_empty = null;
        spyDisplaySeletionModule.displayBranchDetails(mockRepoList, mockOrg, Author_empty, mockStalePeriod);
        verify(spyDisplaySeletionModule).displayBranchDetails(mockRepoList, mockOrg, Author_empty, mockStalePeriod);

        spyDisplaySeletionModule.displayBranchDetails(mockRepoList, mockOrg, mockAuthor, mockStalePeriod);
        verify(spyDisplaySeletionModule).displayBranchDetails(mockRepoList, mockOrg, Author_empty, mockStalePeriod);
    }

    @Test
    public void displayRepoList_Sucess() throws JSONException, IOException
    {
        Repository repo1 = new Repository();
        repo1.setRepoName("business_Logic_Module");

        ArrayList<Repository> mockRepoList = new ArrayList<>();
        mockRepoList.add(repo1);

        when(spyDisplaySeletionModule.getRepoService()).thenReturn(mockRepoService);
        when(mockRepoService.repoUrl(any(Organization.class))).thenReturn("StaleBranchData JSON as String");
        when(spyDisplaySeletionModule.getRepoDetails()).thenReturn(mockRepoDetails);
        when(mockRepoDetails.getRepoInfo(anyString())).thenReturn(mockRepoList);
        when(spyDisplaySeletionModule.getDisplayModule()).thenReturn(mockDisplayModule);
        doNothing().when(mockDisplayModule).displayRepo(mockRepoList);

        spyDisplaySeletionModule.displayRepoList(mockOrg);
        verify(spyDisplaySeletionModule, times(1)).displayRepoList(mockOrg);
    }

    @Test
    public void displayUsersList_Success() throws IOException
    {
        Repository repo1 = new Repository();
        repo1.setRepoName("business_Logic_Module");

        ArrayList<Repository> mockRepoList = new ArrayList<>();
        mockRepoList.add(repo1);

        User mockUser1 = new User();
        mockUser1.setLogin("bc015394");

        User mockUser2 = new User();
        mockUser2.setLogin("rk015945");

        ArrayList<User> usersList = new ArrayList<>();

        when(spyDisplaySeletionModule.getUserService()).thenReturn(mockUserService);
        when(mockUserService.userUrl(anyString(), any(Organization.class))).thenReturn("Some_User_JSON as String");
        when(spyDisplaySeletionModule.getUserDetails()).thenReturn(mockUserDetails);
        when(mockUserDetails.getUserInfo(anyString())).thenReturn(usersList);
        when(spyDisplaySeletionModule.getDisplayModule()).thenReturn(mockDisplayModule);

        doNothing().when(mockDisplayModule).displayUsers(usersList);

        spyDisplaySeletionModule.displayUsersList(mockRepoList, mockOrg);
        verify(spyDisplaySeletionModule, times(1)).displayUsersList(mockRepoList, mockOrg);
    }

    @Test
    public void staleBranchesList_Success() throws Exception
    {
        User mockUser1 = new User();
        mockUser1.setLogin("bc015394");

        User mockUser2 = new User();
        mockUser2.setLogin("rk015945");

        Branch mockStaleBranch = new Branch();
        mockStaleBranch.setBranchName("BugFix_Solution");
        mockStaleBranch.setIsActive(false);
        mockStaleBranch.setUser(mockUser1);

        Branch mockActiveBranch = new Branch();
        mockActiveBranch.setBranchName("Calculate_Age");
        mockStaleBranch.setIsActive(true);
        mockStaleBranch.setUser(mockUser2);

        Branch mockStaleBranch2 = new Branch();
        mockStaleBranch.setBranchName("BugFix_Solutions");
        mockStaleBranch.setIsActive(false);
        mockStaleBranch.setUser(mockUser1);

        ArrayList<Branch> mockStaleBranchList = new ArrayList<>();
        mockStaleBranchList.add(mockStaleBranch);
        mockStaleBranchList.add(mockStaleBranch2);

        Repository repo1 = new Repository();
        repo1.setRepoName("business_Logic_Module");

        ArrayList<Repository> mockRepoList = new ArrayList<>();
        mockRepoList.add(repo1);

        String ExpectedMailBody = "Mail Content as String";

        when(spyDisplaySeletionModule.getStaleBranches()).thenReturn(mockStaleBranches);
        when(mockStaleBranches.retrieveStaleBranches(anyList(), any(Organization.class), anyInt()))
                .thenReturn(mockRepoList);
        when(spyDisplaySeletionModule.getMailContent()).thenReturn(mockMailContent);
        when(mockMailContent.prepareEmailContent(anyList(), any(Organization.class), anyInt()))
                .thenReturn("Mail Content as String");

        assertEquals(ExpectedMailBody, spyDisplaySeletionModule.displayStaleBranches(mockRepoList, mockOrg, 0));
        mockRepoList.clear();
        assertEquals(null, spyDisplaySeletionModule.displayStaleBranches(mockRepoList, mockOrg, 0));
    }
}
