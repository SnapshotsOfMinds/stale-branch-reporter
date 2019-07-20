package com.github.report.exception;

/**
 * An exception which indicates an error attempting to retrieve data from a rest service.
 */
public class RestServiceException extends Exception {
  /**
   * Auto generated serial version UID
   */
  private static final long serialVersionUID = -3968048925756621428L;

  /**
   * Create an exception with no context.
   */
  public RestServiceException() {
    super();
  }

  /**
   * Create an instance of the exception with a message.
   *
   * @param message The message associated with the exception.
   */
  public RestServiceException(String message) {
    super(message);
  }

  /**
   * Create an instance of the exception with a reference to a {@link Throwable} cause. The
   * Throwable's message will be logged as the message.
   *
   * @param cause The cause of the exception.
   */
  public RestServiceException(Throwable cause) {
    super(cause.getMessage(), cause);
  }

  /**
   * Create an instance of the exception with a message and a reference to a {@link Throwable} cause.
   *
   * @param message The message associated with the exception.
   * @param cause The cause of the exception.
   */
  public RestServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
