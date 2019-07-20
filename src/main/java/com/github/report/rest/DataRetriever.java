package com.github.report.rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import com.github.report.exception.RestServiceException;
import com.github.report.tool.I18NMessageUtility;

/**
 * Super class for the data retrievers that contains common functionality the children use.
 */
public abstract class DataRetriever {
  private static final I18NMessageUtility I18N_MESSAGE_UTILITY = new I18NMessageUtility();

  private static final String CONTENT_TYPE_KEY = "Content-type";
  private static final String CONTENT_TYPE_VALUE = "application/json";
  private static final String ACCEPT_KEY = "accept";
  private static final String ACCEPT_VALUE = "application/json";
  private static final String GET = "GET";
  private static final boolean USER_INTERACTION_FLAG = false;

  /**
   * Retrieve the {@link HttpsURLConnection} for the provided {@link URL}.
   *
   * @param url The URL to connect with.
   * @return The HttpsURLConnection with valid properties configured.
   * @throws IOException If unable to open the connection.
   */
  public HttpsURLConnection getURLConnection(URL url) throws IOException {
    HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
    urlConn.setRequestProperty(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);
    urlConn.setRequestProperty(ACCEPT_KEY, ACCEPT_VALUE);
    urlConn.setRequestMethod(GET);
    urlConn.setAllowUserInteraction(USER_INTERACTION_FLAG);

    return urlConn;
  }

  /**
   * Wrapper method to retrieve the {@link URL} object based off the provided URL string.
   *
   * @param url The URL string to compose the URL.
   * @return The generated URL object.
   * @throws RestServiceException If unable to create a URL object from the given string.
   */
  protected URL getURL(String url) throws RestServiceException {
    try {
      return new URL(url);
    } catch (MalformedURLException e) {
      throw new RestServiceException(I18N_MESSAGE_UTILITY.getMessage("FAILURE_CREATING_URL", url), e);
    }
  }
}
