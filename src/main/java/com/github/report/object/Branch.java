package com.github.report.object;

import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Object representation of a Branch.
 */
public class Branch {
  private String name;
  private Commit commit;
  private boolean isActive;
  private String inactiveAge;
  private JiraStory jiraStory;

  private Branch(Builder b) {
    this.name = b.name;
    this.commit = b.commit;
    this.isActive = b.isActive;
    this.inactiveAge = b.inactiveAge;
    this.jiraStory = b.jiraStory;
  }

  /**
   * Get the branch's name.
   *
   * @return The branch's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the branch's most recent {@link Commit}.
   *
   * @return The branch's most recent Commit.
   */
  public Commit getMostRecentCommit() {
    return commit;
  }

  /**
   * Returns whether or not the branch is active.
   *
   * @return {@code True} if the branch is active, {@code false} otherwise.
   */
  public boolean isActive() {
    return isActive;
  }

  /**
   * Get the time since the branch has been active.
   *
   * @return The time since the branch has been active.
   */
  public String getInactiveAge() {
    return inactiveAge;
  }

  /**
   * Gets the {@link JiraStory} that corresponds to this branch.
   *
   * @return The corresponding JiraStory to this branch.
   */
  public JiraStory getJiraStory() {
    return jiraStory;
  }

  /**
   * Builder class used to create a Branch object.
   */
  public static class Builder {
    private String name;
    private Commit commit;
    private boolean isActive;
    private String inactiveAge;
    private JiraStory jiraStory;

    /**
     * Default constructor.
     */
    public Builder() {
      name = "";
      commit = null;
      isActive = false;
      inactiveAge = "";
      this.jiraStory = null;
    }

    /**
     * Constructor used to create a new Builder class from an existing Branch object.
     *
     * @param branch The Branch object that will be used to populate the values in the Builder class.
     */
    public Builder(Branch branch) {
      this.name = branch.name;
      this.commit = branch.commit;
      this.isActive = branch.isActive;
      this.inactiveAge = branch.inactiveAge;
      this.jiraStory = branch.jiraStory;
    }

    /**
     * Setter to set the name.
     *
     * @param name The branch's name.
     * @return The builder object with the name set.
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Setter to set the most recent {@link Commit}.
     *
     * @param commit The branch's most recent Commit.
     * @return The builder object with the most recent Commit set.
     */
    public Builder commit(Commit commit) {
      this.commit = commit;
      return this;
    }

    /**
     * Setter to set the if the branch is active.
     *
     * @param isActive The boolean describing if the branch is active.
     * @return The builder object with the active indicator set.
     */
    public Builder isActive(boolean isActive) {
      this.isActive = isActive;
      return this;
    }

    /**
     * Setter to set the time since the branch has been active.
     *
     * @param activeAge The time since the branch has been active.
     * @return The builder object with the age set.
     */
    public Builder inactiveAge(String activeAge) {
      this.inactiveAge = activeAge;
      return this;
    }

    /**
     * Setter to the {@link JiraStory} corresponding to the branch.
     *
     * @param jiraStory The JiraStory that corresponds to the branch.
     * @return The builder object with the JiraStory set.
     */
    public Builder jiraStory(JiraStory jiraStory) {
      this.jiraStory = jiraStory;
      return this;
    }

    /**
     * This method calls the constructor for Branch and returns the object that is created.
     *
     * @return The Branch object created from the data in the Builder class.
     */
    public Branch build() {
      return new Branch(this);
    }
  }

  @Override
  public String toString() {
    return "[{Branch} " + "Name: " + name + ", Commit: " + commit + ", Is Active: " + isActive + ", Time Since Active: " + inactiveAge + ", Jira Story: " + jiraStory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Branch)) {
      return false;
    }

    final Branch other = (Branch) o;
    return Objects.equals(this.name, other.name) && Objects.equals(this.commit, other.commit) && this.isActive == other.isActive && Objects.equals(this.inactiveAge, other.inactiveAge)
        && Objects.equals(this.jiraStory, other.jiraStory);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.name).append(commit).append(this.isActive).append(inactiveAge).append(jiraStory).toHashCode();
  }
}
