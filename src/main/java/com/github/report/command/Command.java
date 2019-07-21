package com.github.report.command;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;
import com.beust.jcommander.Parameter;
import com.github.report.CommandMode;
import com.github.report.exception.CommandException;
import com.github.report.json.parser.BranchJSONParser;
import com.github.report.json.parser.RepoJSONParser;
import com.github.report.json.parser.UserJSONParser;
import com.github.report.rest.GitHubDataRetriever;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * The class that defines the basis for a command implementation.
 */
public abstract class Command {
  private static final String MANUAL_EXTENSION = ".man";

  @Parameter(names = {"-h", "--help"}, help = true, order = 10, description = "Display command specific help information.")
  private boolean isHelpRequested;

  /**
   * Execute the command with the command-provided variables.
   *
   * @return Boolean result if the command was executed or not. This does not represent if the command
   *         itself was successful.
   * @throws CommandException If any errors occur while executing a command.
   */
  public abstract boolean execute() throws CommandException;

  /**
   * Display the help manual page for the given command.
   *
   * @param commandMode The {@link CommandMode} to display the manual page for.
   * @throws IOException If the correct manual page cannot be obtained or displayed.
   * @throws CommandException If the provided command does not have a corresponding manual page.
   */
  protected void displayHelpInformation(CommandMode commandMode) throws IOException, CommandException {
    Control control = ResourceBundle.Control.getControl(Control.FORMAT_DEFAULT);
    List<Locale> locales = control.getCandidateLocales(this.getClass().getName(), Locale.getDefault());

    URL url = null;
    for (Locale locale : locales) {
      try {
        String manualPage = commandMode.toCommand();
        url = Resources.getResource(manualPage + (!locale.toString().isEmpty() ? "_" : "") + locale + MANUAL_EXTENSION);
        break;
      } catch (IllegalArgumentException e) {
        continue;
      }
    }
    if (url == null) {
      throw new CommandException();
    }

    System.out.println(Resources.toString(url, Charsets.UTF_8));
  }

  /**
   * Retrieves the boolean value that determines if command specific help information should be
   * displayed. This value will only be true if the help flag was provided as an argument to the
   * command.
   *
   * @return {@code True} if the help flag was included as parameter in the command, {@code false}
   *         otherwise.
   */
  protected boolean displayHelp() {
    return isHelpRequested;
  }

  protected GitHubDataRetriever getGitHubDataRetriever() {
    return new GitHubDataRetriever();
  }

  protected RepoJSONParser getRepoJSONParser() {
    return new RepoJSONParser();
  }

  protected BranchJSONParser getBranchJSONParser(long stalePeriod) {
    return new BranchJSONParser(stalePeriod);
  }

  protected UserJSONParser getUserJSONParser() {
    return new UserJSONParser();
  }
}
