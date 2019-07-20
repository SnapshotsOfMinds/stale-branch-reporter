package com.github.report;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.github.report.validate.FileValidator;

@Parameters(separators = "=")
public class MainCLIParams {
  @Parameter(names = {"-h", "--help"}, help = true, description = "Displays help for input information")
  private boolean help;

  @Parameter(names = {"-o", "--organization"}, required = true, order = 0, description = "Team or Organization Name as displayed in GitHub")
  private String org;

  @Parameter(names = {"-d", "--directoryTeamMail"}, validateWith = FileValidator.class, order = 1, required = false, description = "Absolute path to file containing an email recipient list.")
  private String mailingListFile;

  @Parameter(names = {"-e", "--emails"}, splitter = SemiColonSplitter.class, required = false, description = "Email recipients separated by a semicolon (;).")
  private List<String> emails;

  @Parameter(names = {"-t", "--staleAfterPeriod"}, description = "if the branch has no commits after specified date it will be marked as stale branch", required = false)
  private int stalePeriod;

  @Parameter(names = {"-a", "--author"}, description = "name to fetch Commits from particular Team Member", required = false)
  private String author;

  private Set<String> recipients = new HashSet<>();

  public int getStalePeriod() {
    return stalePeriod;
  }

  public String getOrg() {
    return org;
  }

  public String getAuthor() {
    return author;
  }

  public boolean isHelp() {
    return help;
  }

  public Set<String> getEmailRecipients() {
    if (mailingListFile != null) {
      try {
        this.recipients.addAll(Files.readAllLines(Paths.get(mailingListFile), StandardCharsets.UTF_8).stream().collect(Collectors.toSet()));
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    if (emails != null) {
      this.recipients.addAll(emails);
    }

    return this.recipients;
  }
}
