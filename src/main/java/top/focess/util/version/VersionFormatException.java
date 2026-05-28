package top.focess.util.version;


/**
 * Thrown to indicate that a version string is not in the correct format.
 * @deprecated FocessUtil is no longer maintained. Do not use.
 */
@Deprecated(forRemoval = true, since = "1.1.25")
public class VersionFormatException extends RuntimeException {

    /**
     * Constructs a VersionFormatException
     *
     * @param version the version that is not in correct format
     */
    public VersionFormatException(final String version) {
        super("The format of " + version + " is wrong.");
    }
}
