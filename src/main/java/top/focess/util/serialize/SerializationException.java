package top.focess.util.serialize;

/**
 * Thrown to indicate that an error occurred while serializing or deserializing.
 * @deprecated FocessUtil is no longer maintained. Do not use.
 */
@Deprecated(forRemoval = true, since = "1.1.25")
public class SerializationException extends RuntimeException {
    public SerializationException(Exception e) {
        super(e);
    }
}
