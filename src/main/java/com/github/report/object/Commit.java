package com.github.report.object;

import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Object representation of a Commit.
 */
public class Commit {
  private User author;
  private String date;

  private Commit(Builder b) {
    this.author = b.author;
    this.date = b.date;
  }

  /**
   * Get the commit's author.
   *
   * @return The commit's date.
   */
  public User getAuthor() {
    return author;
  }

  /**
   * Get the commit's date.
   *
   * @return The commit's date.
   */
  public String getDate() {
    return date;
  }

  /**
   * Builder class used to create a Commit object.
   */
  public static class Builder {
    private User author;
    private String date;

    /**
     * Default constructor.
     */
    public Builder() {
      author = null;
      date = "";
    }

    /**
     * Constructor used to create a new Builder class from an existing Commit object.
     *
     * @param commit The Commit object that will be used to populate the values in the Builder class.
     */
    public Builder(Commit commit) {
      this.author = commit.author;
      this.date = commit.date;
    }

    /**
     * Setter to set the author.
     *
     * @param author The commit's author.
     * @return The builder object with the author set.
     */
    public Builder author(User author) {
      this.author = author;
      return this;
    }

    /**
     * Setter to set the date.
     *
     * @param date The date information.
     * @return The builder object with the date set.
     */
    public Builder date(String date) {
      this.date = date;
      return this;
    }

    /**
     * This method calls the constructor for Commit and returns the object that is created.
     *
     * @return The Commit object created from the data in the Builder class.
     */
    public Commit build() {
      return new Commit(this);
    }
  }

  @Override
  public String toString() {
    return "[{Commit} " + "Author: " + author + ", Date: " + date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Commit)) {
      return false;
    }

    final Commit other = (Commit) o;
    return Objects.equals(this.author, other.author) && Objects.equals(this.date, other.date);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.date).append(this.author).append(this.date).toHashCode();
  }
}
