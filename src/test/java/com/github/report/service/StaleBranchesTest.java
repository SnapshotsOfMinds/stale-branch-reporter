package com.github.report.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.github.report.json.parser.BranchJSONParser;
import com.github.report.object.Branch;
import com.github.report.object.Organization;
import com.github.report.object.Repository;
import com.github.report.object.User;

@ExtendWith({ MockitoExtension.class })
public class StaleBranchesTest
{
    @Spy
    private StaleBranches spyStaleBranches;

    @Mock
    private BranchService mockBranchService;
    @Mock
    private BranchJSONParser mockBranchDetails;

    @Test
    public void StaleBranches_success() throws Exception
    {

        int stalePeriod = 30;

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

        when(spyStaleBranches.getBranchService()).thenReturn(mockBranchService);
        when(mockBranchService.getRepoBranches(anyString(), any(Organization.class))).thenReturn("branchasJson");
        when(spyStaleBranches.getBranchDetails()).thenReturn(mockBranchDetails);
        when(mockBranchDetails.getBranchInfo(anyString(), anyInt())).thenReturn(mockBranchList);

        Set<String> emailIDs = new HashSet<>();
        emailIDs.add("anc@abc");
        emailIDs.add("xyz@xyz");

        when(mockBranchDetails.getEmailIDs()).thenReturn(emailIDs);
        Organization org = new Organization();
        org.setOrgName("SomeOrgName");
        ArrayList<Repository> repo_StaleBranch = spyStaleBranches.retrieveStaleBranches(mockRepoList, org, stalePeriod);

        assertEquals("business_Logic_Module", repo_StaleBranch.get(0).getRepoName());
    }
}