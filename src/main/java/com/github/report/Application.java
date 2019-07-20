package com.github.report;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.github.report.command.BranchCommand;
import com.github.report.command.Command;
import com.github.report.command.JiraCommand;
import com.github.report.command.RepositoryCommand;
import com.github.report.command.StaleBranchCommand;
import com.github.report.command.UserCommand;

/**
 * Main class for the reporting tool.
 */
public class Application {
  /**
   * Enum representing the system exit status.
   */
  public static enum Status {
    /**
     * Used when there is an unexpected error parsing the input commands, such as when argument length
     * is 0 or {@link JCommander#getParsedCommand()} returns {@code null}.
     */
    ERROR_INPUT_PARSING(-1),

    /**
     * Used when the system exits successfully without errors.
     */
    SUCCESS(0),

    /**
     * Used when {@link CommandExecutor#execute()} returns false.
     */
    ERROR_INVALID_EXECUTION(1),

    /**
     * Used when an unexpected exception bubbles up to this top level class.
     */
    ERROR_TOP_LEVEL_EXCEPTION(2);

    private final int value;

    /**
     * Constructs the system exit status enum with given integer value.
     *
     * @param value Integer value of the system exit status.
     */
    Status(int value) {
      this.value = value;
    }

    /**
     * Returns the integer value of the system exit status.
     *
     * @return Integer value of the system exit status.
     */
    public int toValue() {
      return value;
    }
  }

  private static JCommander jcommander;

  /**
   * Main entry point into the application.
   *
   * @param args The command line arguments used to determine the flow of control for the application.
   */
  public static void main(String[] args) {
    Application application = new Application(args);
    application.run();
  }

  /**
   * Construct an instance of the Application class. This will create all of the recognized
   * {@link Command Commands} and parse the passed in arguments.
   *
   * @param args The command line arguments used to determine the flow of control for the application.
   */
  public Application(String[] args) {
    jcommander = new JCommander();

    // Set the program name so it displays in the usage information.
    jcommander.setProgramName("github-jira-reporter");
    jcommander.setCaseSensitiveOptions(false);

    // Create the recognized commands.
    createCommands();

    if (args.length == 0) {
      jcommander.usage();
      System.exit(Status.ERROR_INPUT_PARSING.toValue());
    }

    try {
      // Parse the arguments of the command.
      jcommander.parse(args);
    } catch (ParameterException e) {
      System.err.println(e.getMessage());
      if (jcommander.getParsedCommand() != null) {
        jcommander.getCommands().get(jcommander.getParsedCommand()).usage();
      } else {
        jcommander.usage();
      }
      System.exit(Status.ERROR_INPUT_PARSING.toValue());
    }
  }

  /**
   * Perform the necessary work to execute the passed-in, parsed {@link Command}.
   */
  public void run() {
    try {
      System.exit(executeCommand());
    } catch (SecurityException e) {
      // This isn't a bad exception. The testing framework throws this exception even when the application
      // successfully exits.
      System.err.println("Exception caught while trying to exit the application.");
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println("Unexpected error occurred during execution. Application is terminating.");
      e.printStackTrace();
      System.exit(Status.ERROR_TOP_LEVEL_EXCEPTION.toValue());
    }
  }

  /**
   * Create the recognized {@link Command}s and add them to the {@link JCommander} object.
   */
  private void createCommands() {
    jcommander.addCommand(CommandMode.BRANCHES.toCommand(), new BranchCommand());
    jcommander.addCommand(CommandMode.JIRA_STORY.toCommand(), new JiraCommand());
    jcommander.addCommand(CommandMode.REPOSITORIES.toCommand(), new RepositoryCommand());
    jcommander.addCommand(CommandMode.STALE_BRANCHES.toCommand(), new StaleBranchCommand());
    jcommander.addCommand(CommandMode.USERS.toCommand(), new UserCommand());
  }

  /**
   * Execute the {@link Command}. If the configuration information has not been set or command
   * execution fails, then an error code will be returned.<br>
   * <br>
   * NOTE: The return status in this case does not describe if the command performed its execution
   * successfully, only that it was successful in calling to execute the command.
   *
   * @return An error code will be returned if the system is not configured or command execution
   *         fails. A success code will be returned if command execution was successful.
   */
  private int executeCommand() {
    CommandExecutor executor = getCommandExecutor();
    if (executor.execute()) {
      return Status.SUCCESS.toValue();
    }

    return Status.ERROR_INVALID_EXECUTION.toValue();
  }

  /**
   * Retrieves an instance of the {@link CommandExecutor} with the {@link Command} populated.
   *
   * @return An instance of the {@link CommandExecutor} with the {@link Command} populated.
   */
  protected CommandExecutor getCommandExecutor() {
    return new CommandExecutor(getCommand());
  }

  /**
   * Retrieve the {@link Command} from the command line arguments.
   *
   * @return The {@link Command} that was requested for execution.
   */
  private Command getCommand() {
    Command command = getParsedCommand();

    if (command == null) {
      jcommander.usage();
      System.exit(Status.ERROR_INPUT_PARSING.toValue());
    }

    return command;
  }

  /**
   * Obtain the {@link Command} from the parsed command line arguments. The command line arguments are
   * returned as a List of {@link Object}s via the getObjects() call. The command itself will be the
   * first object in that list.
   *
   * @return Obtain the {@link Command} from the parsed command line arguments.
   */
  protected Command getParsedCommand() {
    return ((Command) jcommander.getCommands().get(jcommander.getParsedCommand()).getObjects().get(0));
  }
}
