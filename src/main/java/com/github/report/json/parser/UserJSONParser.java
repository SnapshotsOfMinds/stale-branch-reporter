package com.github.report.json.parser;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.github.report.exception.JSONParseException;
import com.github.report.object.User;

/**
 * Class responsible for parsing JSON data into {@link User} objects.
 */
public class UserJSONParser extends JSONParser<User> {
  @Override
  public List<User> parse(String jsonData) throws JSONParseException {
    List<User> userList = new ArrayList<>();

    try {
      JSONArray jsonArray = getJSONArray(jsonData);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonStatusObject = jsonArray.getJSONObject(i);

        userList.add(new User.Builder().login(jsonStatusObject.getString("login")).build());
      }
    } catch (JSONException e) {
      throw new JSONParseException(e);
    }

    return userList;
  }
}
