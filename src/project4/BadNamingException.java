package project4;

/**
 * Bad Naming Exception
 *
 * An exception to be thrown when the user selects a bad name or password.
 *
 * @author Visv Shah
 * @version 11/13/22
 */
public class BadNamingException extends Exception {

    /**
     * Create a new BadNamingException with a given message
     * @param message a string message
     */
    public BadNamingException(String message) {
        super(message);
    }
}