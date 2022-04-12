package top.focess.util.serialize;

/**
 * Thrown to indicate that an error occurred while serializing or deserializing.
 */
public class SerializationException extends RuntimeException {
    public SerializationException(Exception e) {
        super(e);
    }
}
