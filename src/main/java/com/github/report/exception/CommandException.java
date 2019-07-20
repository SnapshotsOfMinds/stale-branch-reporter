package com.github.report.exception;

/**
 * An exception which indicates an error attempting create or send an email.
 */
public class CommandException extends Exception {
  /**
   * Auto generated serial version UID
   */
  private static final long serialVersionUID = -2792788912191171823L;

  /**
   * Create an exception with no context.
   */
  public CommandException() {
    super();
  }

  /**
   * Create an instance of the exception with a message.
   *
   * @param message The message associated with the exception.
   */
  public CommandException(String message) {
    super(message);
  }

  /**
   * Create an instance of the exception with a reference to a {@link Throwable} cause. The
   * Throwable's message will be logged as the message.
   *
   * @param cause The cause of the exception.
   */
  public CommandException(Throwable cause) {
    super(cause.getMessage(), cause);
  }

  /**
   * Create an instance of the exception with a message and a reference to a {@link Throwable} cause.
   *
   * @param message The message associated with the exception.
   * @param cause The cause of the exception.
   */
  public CommandException(String message, Throwable cause) {
    super(message, cause);
  }
}
