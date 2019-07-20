package com.github.report;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.github.report.object.Branch;
import com.github.report.object.Repository;
import com.github.report.object.User;

@ExtendWith({ MockitoExtension.class })
public class DisplayModuleTest
{
    @Spy
    private DisplayModule spyDisplayModule;

    @Test
    public void displayRepo_succes()
    {
        ArrayList<Repository> repoList = new ArrayList<>();
        Repository repo1 = new Repository();
        repo1.setRepoName("repo1");
        repoList.add(repo1);
        spyDisplayModule.displayRepo(repoList);
        verify(spyDisplayModule, times(1)).displayRepo(repoList);
    }

    @Test
    public void displayUsers_success()
    {
        ArrayList<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setLogin("AA001122");
        userList.add(user1);
        spyDisplayModule.displayUsers(userList);
        verify(spyDisplayModule, times(1)).displayUsers(userList);
    }

    @Test
    public void displayBranch_succes()
    {
        Branch branch = new Branch();

        User user1 = new User();
        user1.setLogin("AA001122");
        user1.setEmail("abc@abc");
        user1.setName("UserA");

        branch.setBranchName("BranchA");
        branch.setIsActive(false);
        branch.setUser(user1);
        branch.setDate("2016-12-02T18:31:34Z");
        branch.setTimeStamp("6 Months 26 Days");

        spyDisplayModule.displayBranch(branch);
        verify(spyDisplayModule, times(1)).displayBranch(branch);
    }
}
