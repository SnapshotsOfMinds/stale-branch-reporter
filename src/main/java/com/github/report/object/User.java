package com.github.report.object;

import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 */
public class User {
  private String login;
  private String name;
  private String email;
  private String avatar;

  private User(Builder b) {
    this.login = b.login;
    this.name = b.name;
    this.email = b.email;
    this.avatar = b.avatar;
  }

  /**
   * Get the user's login.
   *
   * @return The user's login.
   */
  public String getLogin() {
    return login;
  }

  /**
   * Get the user's name.
   *
   * @return The user's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the user's email.
   *
   * @return The user's email.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Get the user's avatar URL.
   *
   * @return The user's avatar URL.
   */
  public String getAvatar() {
    return avatar;
  }

  /**
   * Builder class used to create a User object.
   */
  public static class Builder {
    private String login;
    private String name;
    private String email;
    private String avatar;

    /**
     * Default constructor.
     */
    public Builder() {
      this.login = "";
      this.name = "";
      this.email = "";
      this.avatar = "";
    }

    /**
     * Constructor used to create a new Builder class from an existing User object.
     *
     * @param user The user object that will be used to populate the values in the Builder class.
     */
    public Builder(User user) {
      this.login = user.login;
      this.name = user.name;
      this.email = user.email;
      this.avatar = user.avatar;
    }

    /**
     * Setter to set the login.
     *
     * @param login The login information.
     * @return The builder object with the login set.
     */
    public Builder login(String login) {
      this.login = login;
      return this;
    }

    /**
     * Setter to set the name.
     *
     * @param name The user's name.
     * @return The builder object with the name set.
     */
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Setter to set the user's email.
     *
     * @param email The user's email.
     * @return The builder object with the email set.
     */
    public Builder email(String email) {
      this.email = email;
      return this;
    }

    /**
     * Setter to set the user's avatar URL.
     *
     * @param avatar The user's avatar URL.
     * @return The builder object with the avatar URL set.
     */
    public Builder avatar(String avatar) {
      this.avatar = avatar;
      return this;
    }

    /**
     * This method calls the constructor for User and returns the object that is created.
     *
     * @return The User object created from the data in the Builder class.
     */
    public User build() {
      return new User(this);
    }
  }

  @Override
  public String toString() {
    return "[{User} " + "Login: " + login + "Name: " + name + "Email: " + email + "Avatar URL: " + avatar;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof User)) {
      return false;
    }

    final User other = (User) o;
    return Objects.equals(this.login, other.login) && Objects.equals(this.name, other.name) && Objects.equals(this.email, other.email) && Objects.equals(this.avatar, other.avatar);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.login).append(this.name).append(this.email).append(this.avatar).toHashCode();
  }
}
