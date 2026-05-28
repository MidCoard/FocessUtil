package top.focess.util.serialize;

/**
 * Thrown to indicate that an object is not serializable.
 * @deprecated FocessUtil is no longer maintained. Do not use.
 */
@Deprecated(forRemoval = true, since = "1.1.25")
public class NotFocessSerializableException extends RuntimeException {

    /**
     * Constructs a NotFocessSerializableException
     *
     * @param cls the class that is not serializable
     */
    public NotFocessSerializableException(final String cls) {
        super("The class " + cls + " is not FocessSerializable");
    }
}
