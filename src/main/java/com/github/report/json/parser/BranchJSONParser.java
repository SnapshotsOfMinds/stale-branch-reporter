package com.github.report.json.parser;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.github.report.exception.JSONParseException;
import com.github.report.exception.RestServiceException;
import com.github.report.object.Branch;
import com.github.report.object.Commit;
import com.github.report.object.User;
import com.github.report.rest.GitHubDataRetriever;
import com.github.report.tool.I18NMessageUtility;

/**
 * Class responsible for parsing JSON data into {@link Branch} objects. This includes the most
 * recent {@link Commit} object for the branch and the {@link User} which authored the commit.
 */
public class BranchJSONParser extends JSONParser<Branch> {
  private static final I18NMessageUtility I18N_MESSAGE_UTILITY = new I18NMessageUtility();

  private static final String COMMIT_TAG = "commit";
  private static final String AUTHOR_TAG = "author";
  private static final String COMMITTER_TAG = "committer";
  private static final String EMAIL_TAG = "email";
  private static final String NAME_TAG = "name";
  private static final String LOGIN_TAG = "login";
  private static final String AVATAR_TAG = "avatar_url";
  private static final String URL_TAG = "url";
  private static final String DATE_TAG = "date";
  private static final String HTML_TAG = "html_url";

  private static final String INVALID_USER = "InvalidUser";

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private long timePeriod;
  private long days;

  /**
   * Construct a {@code BranchJSONParser} for the purpose of parsing JSON data into a {@link Branch}
   * object, including the composing objects.
   *
   * @param timePeriod The time period to use to determine if a Branch is active.
   */
  public BranchJSONParser(long timePeriod) {
    this.timePeriod = timePeriod;
  }

  @Override
  public List<Branch> parse(String jsonData) throws JSONParseException {
    JSONArray jsonArray = new JSONArray(jsonData);
    List<Branch> branches = new ArrayList<>();

    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject jsonStatusObject = jsonArray.getJSONObject(i);

      String commitJSONData;
      try {
        commitJSONData = getGitHubDataRetriever().retrieveCommit(jsonStatusObject.getJSONObject(COMMIT_TAG).getString(URL_TAG));
      } catch (RestServiceException e) {
        throw new JSONParseException(e);
      }

      JSONObject jsonCommitObject = new JSONObject(commitJSONData);

      String jsonTag = "";
      if (!jsonCommitObject.isNull(AUTHOR_TAG)) {
        jsonTag = AUTHOR_TAG;
      } else if (!jsonCommitObject.isNull(COMMITTER_TAG)) {
        jsonTag = COMMITTER_TAG;
      }

      User user = new User.Builder().name(jsonCommitObject.getJSONObject(COMMIT_TAG).getJSONObject(AUTHOR_TAG).getString(NAME_TAG))
          .email(jsonCommitObject.getJSONObject(COMMIT_TAG).getJSONObject(AUTHOR_TAG).getString(EMAIL_TAG))
          .login(!jsonTag.isEmpty() ? jsonCommitObject.getJSONObject(jsonTag).getString(LOGIN_TAG) : INVALID_USER)
          .avatar(!jsonTag.isEmpty() ? jsonCommitObject.getJSONObject(jsonTag).getString(AVATAR_TAG) : "").build();

      String commitDate = jsonCommitObject.getJSONObject(COMMIT_TAG).getJSONObject(AUTHOR_TAG).getString(DATE_TAG);
      Commit commit = new Commit.Builder().author(user).date(commitDate).link(jsonCommitObject.getString(HTML_TAG)).build();

      branches.add(new Branch.Builder().name(jsonStatusObject.getString(NAME_TAG)).commit(commit).isActive(isBranchActive()).inactiveAge(caclulateBranchAge(commitDate)).build());
    }

    return branches;
  }

  private String caclulateBranchAge(String date) {
    LocalDate today = LocalDate.now();
    LocalDate commitDate = LocalDate.parse(date.substring(0, 10), FORMATTER);
    Period period = Period.between(commitDate, today);

    days = ChronoUnit.DAYS.between(commitDate, today);

    String age = "";
    if (period.getYears() != 0) {
      age += period.getYears() + " " + I18N_MESSAGE_UTILITY.getMessage("YEARS");
    }
    if (period.getMonths() != 0) {
      age += period.getMonths() + " " + I18N_MESSAGE_UTILITY.getMessage("MONTHS");
    }
    if (period.getDays() != 0) {
      age += period.getDays() + " " + I18N_MESSAGE_UTILITY.getMessage("DAYS");
    }

    return age;
  }

  private boolean isBranchActive() {
    return days >= timePeriod;
  }

  GitHubDataRetriever getGitHubDataRetriever() {
    return new GitHubDataRetriever();
  }
}
