package top.focess.util.option;

/**
 * Thrown to indicate that an option is not supported.
 * @deprecated FocessUtil is no longer maintained. Do not use.
 */
@Deprecated(forRemoval = true, since = "1.1.25")
public class UnsupportedOptionException extends UnsupportedOperationException {

    /**
     * Constructs an UnsupportedOptionException
     * @param option the option that is not supported
     */
    public UnsupportedOptionException(final String option) {
        super("The option " + option + " is not supported.");
    }
}
