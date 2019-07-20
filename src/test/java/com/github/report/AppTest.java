package com.github.report;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.report.email.FormEmail;
import com.github.report.object.Organization;
import com.github.report.service.OrganisationService;
import com.github.report.service.StaleBranches;

@ExtendWith({ MockitoExtension.class })
public class AppTest
{
    @Spy
    private App spyAppication;

    @Mock
    private FormEmail mockFormEmail;
    @Mock
    private OrganisationService mockOrganisationService;
    @Mock
    private StaleBranches mockStaleBranches;
    @Mock
    private DisplaySelectionModule mockDisplaySelectionModule;
    @Mock
    private MainCLIParams mockmainArguments;
    @Mock
    private Organization mockOrg;

    @Test
    public void run_Success() throws IOException
    {
        when(spyAppication.getOrganizationService()).thenReturn(mockOrganisationService);
        when(mockmainArguments.getOrg()).thenReturn("SomeOrgName");
        when(mockOrganisationService.orgUrl(anyString())).thenReturn(mockOrg);

        when(mockmainArguments.getStalePeriod()).thenReturn(0);

        when(spyAppication.getDisplaySelectionModule()).thenReturn(mockDisplaySelectionModule);
        when(mockmainArguments.getEmailIds()).thenReturn(Arrays.asList("email1", "email2", "email3"));
        when(mockDisplaySelectionModule.displayStaleBranches(anyList(), any(Organization.class), anyInt()))
                .thenReturn("mailBody_as_String");
        when(spyAppication.getStaleBranches()).thenReturn(mockStaleBranches);
        Set<String> hash_Set = new HashSet<>();
        hash_Set.add("id1");
        hash_Set.add("id2");
        hash_Set.add("id3");
        when(mockStaleBranches.getMailingList()).thenReturn(hash_Set);
        when(spyAppication.getFormEmail()).thenReturn(mockFormEmail);
        doNothing().when(mockDisplaySelectionModule).displayUsersList(anyList(), any(Organization.class));

        spyAppication.run(mockmainArguments);
        verify(spyAppication, times(1)).run(mockmainArguments);
    }
}
