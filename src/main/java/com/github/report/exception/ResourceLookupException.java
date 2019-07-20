package com.github.report.exception;

/**
 * An exception which indicates an error attempting to lookup resources.
 */
public class ResourceLookupException extends RuntimeException {
  /**
   * Auto generated serial version UID
   */
  private static final long serialVersionUID = -2076796963807589482L;

  /**
   * Create an exception with no context.
   */
  public ResourceLookupException() {
    super();
  }

  /**
   * Create an instance of the exception with a message.
   *
   * @param message The message associated with the exception.
   */
  public ResourceLookupException(String message) {
    super(message);
  }

  /**
   * Create an instance of the exception with a reference to a {@link Throwable} cause. The
   * Throwable's message will be logged as the message.
   *
   * @param cause The cause of the exception.
   */
  public ResourceLookupException(Throwable cause) {
    super(cause.getMessage(), cause);
  }

  /**
   * Create an instance of the exception with a message and a reference to a {@link Throwable} cause.
   *
   * @param message The message associated with the exception.
   * @param cause The cause of the exception.
   */
  public ResourceLookupException(String message, Throwable cause) {
    super(message, cause);
  }
}
