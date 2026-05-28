package top.focess.util.serialize;

/**
 * Thrown to indicate that a serialization parse error has occurred.
 * @deprecated FocessUtil is no longer maintained. Do not use.
 */
@Deprecated(forRemoval = true, since = "1.1.25")
public class SerializationParseException extends RuntimeException {
    /**
     * Constructs a SerializationParseException
     *
     * @param message the detail message
     */
    public SerializationParseException(final String message) {
        super(message);
    }

    /**
     * Constructs a SerializationParseException
     *
     * @param e the cause
     */
    public SerializationParseException(final Exception e) {
        super(e);
    }
}
