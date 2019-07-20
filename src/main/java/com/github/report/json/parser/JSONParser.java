package com.github.report.json.parser;

import java.util.List;
import org.json.JSONArray;
import com.github.report.exception.JSONParseException;

/**
 * Abstract class for the JSON Parsers.
 *
 * @param <T> The object type the parser will parse objects into.
 */
public abstract class JSONParser<T> {
  /**
   * Parse the JSON data into a {@link List} data based on the generic type provided.
   *
   * @param jsonData JSON Data containing the information to parse.
   * @return A List containing fully parsed information.
   * @throws JSONParseException If unable to parse the JSON string.
   */
  public abstract List<T> parse(String jsonData) throws JSONParseException;

  /**
   * Wrapper to get a {@link JSONArray} object with the provided JSON string.
   *
   * @param jsonData The data used to create the JSONArray.
   * @return The created JSONArray object.
   */
  JSONArray getJSONArray(String jsonData) {
    return new JSONArray(jsonData);
  }
}
