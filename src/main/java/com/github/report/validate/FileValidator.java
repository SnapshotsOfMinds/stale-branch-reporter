package com.github.report.validate;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import com.github.report.tool.I18NMessageUtility;

/**
 * Validates any file provided exists.
 */
public class FileValidator implements IParameterValidator {
  private static final I18NMessageUtility I18N_MESSAGE_UTILITY = new I18NMessageUtility();

  @Override
  public void validate(String name, String value) throws ParameterException {
    if (value == null || value.isEmpty()) {
      throw new ParameterException(I18N_MESSAGE_UTILITY.getMessage("NULL_OR_EMPTY_PARAMETER", name));
    }

    try {
      if (!Files.exists(Paths.get(value))) {
        throw new ParameterException(I18N_MESSAGE_UTILITY.getMessage("FILE_DOES_NOT_EXIST", value, name));
      }
    } catch (InvalidPathException e) {
      throw new ParameterException(e);
    }
  }
}
