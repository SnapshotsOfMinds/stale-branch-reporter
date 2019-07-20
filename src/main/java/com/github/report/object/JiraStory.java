package com.github.report.object;

import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents Jira story.
 */
public class JiraStory {
  private String storyKey;
  private String status;

  /**
   * Construct a {@code JiraStory} with the provided key and status.
   *
   * @param storyKey The unique identifier of a {@code JiraStory}
   * @param status The status of the {@code JiraStory}.
   */
  public JiraStory(String storyKey, String status) {
    this.storyKey = storyKey;
    this.status = status;
  }

  /**
   * Gets the unique identifier of the {@code JiraStory}.
   *
   * @return The {@code JiraStory's} unique identifier.
   */
  public String getStoryKey() {
    return storyKey;
  }

  /**
   * Gets the status of the {@code JiraStory}.
   *
   * @return The status of the {@code JiraStory}.
   */
  public String getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return "[{JiraStory} " + "Story Identifier: " + storyKey + ", Status: " + status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof JiraStory)) {
      return false;
    }

    final JiraStory other = (JiraStory) o;
    return Objects.equals(this.storyKey, other.storyKey) && Objects.equals(this.status, other.status);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.storyKey).append(status).toHashCode();
  }
}
