package top.focess.util.option;

/**
 * Thrown to indicate that an option is not supported.
 */
public class UnsupportedOptionException extends UnsupportedOperationException {

    /**
     * Constructs an UnsupportedOptionException
     * @param option the option that is not supported
     */
    public UnsupportedOptionException(final String option) {
        super("The option " + option + " is not supported.");
    }
}
