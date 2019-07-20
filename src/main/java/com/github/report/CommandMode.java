package com.github.report;

import java.util.Objects;
import com.github.report.command.Command;

/**
 * The different modes that are valid for this application.
 */
public enum CommandMode {
  /**
   * Command to retrieve the GitHub branches for each repository.
   */
  BRANCHES("branches"),

  /**
   * Command to retrieve the Jira stories.
   */
  JIRA_STORY("jira-story"),

  /**
   * No command.
   */
  NONE(""),

  /**
   * Command to retrieve the GitHub repositories.
   */
  REPOSITORIES("repositories"),

  /**
   * Command to retrieve the stale GitHub branches.
   */
  STALE_BRANCHES("stale-branches"),

  /**
   * Command to retrieve the users of the repository.
   */
  USERS("users");

  private final String commandText;

  private CommandMode(String commandMode) {
    this.commandText = commandMode;
  }

  /**
   * Return the mode associated with the string given matching the appropriate, unique, value. If
   * nothing matches, {@link #NONE} will be returned.
   *
   * @param value Parameter to find the match for.
   * @return The matching mode or NONE if nothing matches.
   */
  public static CommandMode fromValue(String value) {
    for (CommandMode commandMode : CommandMode.values()) {
      if (Objects.equals(value, commandMode.toCommand())) {
        return commandMode;
      }
    }

    return NONE;
  }

  /**
   * Retrieve the string representation of the {@link Command}.
   *
   * @return The string representation of the command.
   */
  public String toCommand() {
    return commandText;
  }
}
