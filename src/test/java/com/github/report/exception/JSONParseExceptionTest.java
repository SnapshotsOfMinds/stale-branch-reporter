package com.github.report.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Test class for the {@link JSONParseException}.
 */
public class JSONParseExceptionTest {
  private final String TEST_MESSAGE = "ThIs MESSage IS a test.";
  private final String THROWABLE_MESSAGE = "THIS Is A THROWable message";

  /**
   * Test construction using the empty constructor.
   */
  @Test
  public void validateEmptyConstructor() {
    JSONParseException e1 = new JSONParseException();

    assertEquals(null, e1.getMessage());
  }

  /**
   * Test construction using the {@link String} constructor.
   */
  @Test
  public void validateStringConstructor() {
    JSONParseException e1 = new JSONParseException(TEST_MESSAGE);

    assertEquals(TEST_MESSAGE, e1.getMessage());
  }

  /**
   * Test construction using the {@link Throwable} constructor.
   */
  @Test
  public void validateThrowableConstructor() {
    Throwable t1 = new Throwable(THROWABLE_MESSAGE);
    JSONParseException e1 = new JSONParseException(t1);

    assertEquals(t1, e1.getCause());
    assertEquals(THROWABLE_MESSAGE, e1.getMessage());
  }

  /**
   * Test construction using the {@link String} and {@link Throwable} constructor.
   */
  @Test
  public void validateMessageThrowableConstructor() {
    Throwable t1 = new Throwable(THROWABLE_MESSAGE);
    JSONParseException e1 = new JSONParseException(TEST_MESSAGE, t1);

    assertEquals(t1, e1.getCause());
    assertEquals(TEST_MESSAGE, e1.getMessage());
  }
}
