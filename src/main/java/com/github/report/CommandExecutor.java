package com.github.report;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.github.report.command.Command;
import com.github.report.exception.CommandException;
import com.github.report.tool.I18NMessageUtility;

/**
 * Class used to execute the request from a user with a given {@link Command}.
 */
public class CommandExecutor {
  private static final Logger LOGGER = LogManager.getLogger();
  private static final I18NMessageUtility I18N_MESSAGE_UTILITY = new I18NMessageUtility();

  private Command command;

  /**
   * Create an instance of the CommandExecutor to execute the provided {@link Command}.
   *
   * @param command The {@link Command} to be executed.
   */
  public CommandExecutor(Command command) {
    this.command = command;
  }

  /**
   * Execute the {@link Command} given in the constructor. Any error will sent to standard error.
   *
   * @return {@code True} if no errors or exceptions were encountered executing the request with the
   *         given context. {@code False} otherwise.
   */
  public boolean execute() {
    boolean status;
    try {
      status = command.execute();
    } catch (CommandException ex) {
      System.err.println(ex.getMessage());
      LOGGER.error(I18N_MESSAGE_UTILITY.getMessage("ERROR_EXECUTING_COMMAND", command.toString()), ex);
      return false;
    }

    return status;
  }
}
