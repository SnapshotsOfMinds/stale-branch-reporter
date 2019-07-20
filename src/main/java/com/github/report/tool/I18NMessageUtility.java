package com.github.report.tool;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.util.StackLocatorUtil;

import com.github.report.exception.ResourceLookupException;

/**
 * Utility class for logging messages obtained from the properties file
 */
public final class I18NMessageUtility
{
    private final Class<?> clazz;

    /**
     * Constructor to create a I18NMessageUtility object. The {@code clazz} value is read from the call stack within this constructor.
     */
    public I18NMessageUtility()
    {
        this.clazz = StackLocatorUtil.getCallerClass(2);
    }

    /**
     * Constructor to create a I18NMessageUtility object.
     *
     * @param clazz The class used when looking up resources in the i18n properties file.
     */
    public I18NMessageUtility(Class<?> clazz)
    {
        this.clazz = clazz;
    }

    /**
     * Returns the message, with it's variables replaced, from the properties file.
     *
     * @param key The key used to lookup the message.
     * @param locale The {@link Locale} to use when resolving the appropriate bundle.
     * @param values The array of values used to populate variables in the message.
     * @return The formatted string of the original message. If the value obtained from the file is null, then the
     *         empty string will be returned.
     * @throws ResourceLookupException If the resource does not exist in the bundle.
     */
    public String getMessage(String key, Locale locale, Object... values) throws ResourceLookupException
    {
        ResourceBundle rb = ResourceBundle.getBundle("i18n-resources", locale);

        String message;
        try
        {
            message = rb.getString(clazz.getName() + "." + key);
        }
        catch (MissingResourceException e)
        {
            throw new ResourceLookupException(MessageFormat.format("The given key {0}.{1} was not found in the resource file.", clazz, key));
        }

        return MessageFormat.format(message, values);
    }

    /**
     * Returns the message, with it's variables replaced, from the properties file. This method uses the default {@link Locale} while performing the lookup.
     *
     * @param key The key used to lookup the message.
     * @param values The array of values used to populate variables in the message.
     * @return The formatted string of the original message. If the value obtained from the file is null, then the
     *         empty string will be returned.
     * @throws ResourceLookupException If the resource does not exist in the bundle.
     */
    public String getMessage(String key, Object... values) throws ResourceLookupException
    {
        return getMessage(key, Locale.getDefault(), values);
    }
}