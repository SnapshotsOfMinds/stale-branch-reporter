package com.github.report.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.github.report.exception.JSONParseException;
import com.github.report.exception.RestServiceException;
import com.github.report.model.Branch;
import com.github.report.object.User;
import com.github.report.rest.GitHubDataRetriever;

/**
 *
 */
public class BranchDetails {
  private ResourceBundle messageBundle = ResourceBundle.getBundle("MessageBundle", Locale.getDefault());
  private static Set<String> emailIDs = new HashSet<>();

  /**
   * @param branchJSONData
   * @param stalePeriod
   * @return
   * @throws JSONException
   * @throws RestServiceException
   */
  public List<Branch> getBranchInfo(String branchJSONData, int stalePeriod) throws JSONParseException, RestServiceException {

    JSONArray jsonArray = new JSONArray(branchJSONData);
    List<Branch> branchList = new ArrayList<>();
    Map<String, JSONObject> branchMap = new HashMap<>();

    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject jsonStatusObject = jsonArray.getJSONObject(i);

      String branchName = jsonStatusObject.getString("name");
      branchMap.put(branchName, jsonStatusObject);
    }

    // String masterSha = branchMap.get("master").getJSONObject("commit").getString("sha");
    List<JSONObject> jsonObjects = branchMap.entrySet().stream()
        // .filter(entry ->
        // !entry.getValue().getJSONObject("commit").getString("sha").equalsIgnoreCase(masterSha))
        .filter(entry -> !entry.getKey().equalsIgnoreCase("master")).map(Map.Entry::getValue).collect(Collectors.toList());

    User user;
    Branch branch;
    for (JSONObject jsonStatusObject : jsonObjects) {
      branch = new Branch();

      String branchName = jsonStatusObject.getString("name");
      branch.setBranchName(branchName);
      String branchCommitsUrl = jsonStatusObject.getJSONObject("commit").getString("url");
      String commitJSONData = getGitHubDataRetriever().retrieveCommits(branchCommitsUrl);
      JSONObject jsonCommitObject = new JSONObject(commitJSONData);
      branch.setBranchLink(jsonCommitObject.getString("html_url"));
      String emailID = jsonCommitObject.getJSONObject("commit").getJSONObject("author").getString("email");

      if (emailID.contains("@cerner.com")) {
        emailIDs.add(emailID);
      }

      String userName = jsonCommitObject.getJSONObject("commit").getJSONObject("author").getString("name");

      branch.setDate(jsonCommitObject.getJSONObject("commit").getJSONObject("author").getString("date"));

      caclulateBranchAge(branch, stalePeriod);

      String userLogin;
      String userAvatar;
      if (!jsonCommitObject.isNull("author")) {
        userLogin = jsonCommitObject.getJSONObject("author").getString("login");
        userAvatar = jsonCommitObject.getJSONObject("author").getString("avatar_url");

      } else if (!jsonCommitObject.isNull("committer")) {
        userLogin = jsonCommitObject.getJSONObject("committer").getString("login");
        userAvatar = jsonCommitObject.getJSONObject("committer").getString("avatar_url");
      } else {
        userLogin = "InvalidUser";
        userAvatar = "";
      }
      user = new User.Builder().login(userLogin).name(userName).email(emailID).avatar(userAvatar).build();
      branch.setUser(user);
      branchList.add(branch);
    }
    return branchList;
  }

  public Set<String> getEmailIDs() {
    return emailIDs;
  }

  private void caclulateBranchAge(Branch branch, int stalePeriod) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate today = LocalDate.now();
    LocalDate commitDate = LocalDate.parse(branch.getDate().substring(0, 10), formatter);
    Period p = Period.between(commitDate, today);

    long p2 = ChronoUnit.DAYS.between(commitDate, today);

    StringBuilder age = new StringBuilder(" ");
    if (p.getYears() != 0) {
      age.append(p.getYears() + " " + messageBundle.getString("Years"));
    }
    if (p.getMonths() != 0) {
      age.append(p.getMonths() + " " + messageBundle.getString("Months"));
    }
    if (p.getDays() != 0) {
      age.append(p.getDays() + " " + messageBundle.getString("Days"));
    }

    branch.setTimeStamp(age.toString());

    if (p2 >= stalePeriod) {
      branch.setIsActive(false);
    } else {
      branch.setIsActive(true);
    }

  }

  GitHubDataRetriever getGitHubDataRetriever() {
    return new GitHubDataRetriever();
  }
}
