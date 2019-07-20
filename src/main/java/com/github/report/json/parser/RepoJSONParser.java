package com.github.report.json.parser;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.github.report.exception.JSONParseException;
import com.github.report.object.Repository;

/**
 * Class responsible for parsing JSON data into {@link Repository} objects.
 */
public class RepoJSONParser extends JSONParser<Repository> {
  @Override
  public List<Repository> parse(String jsonData) throws JSONParseException {
    List<Repository> repos = new ArrayList<>();

    try {
      JSONArray jsonArray = getJSONArray(jsonData);

      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject json = jsonArray.getJSONObject(i);

        repos.add(new Repository(json.getString("name")));
      }
    } catch (JSONException e) {
      throw new JSONParseException(e);
    }

    return repos;
  }
}
