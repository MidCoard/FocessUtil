package top.focess.util.json;

/**
 * Thrown to indicate JSON parsing error
 * @deprecated FocessUtil is no longer maintained. Do not use.
 */
@Deprecated(forRemoval = true, since = "1.1.25")
public class JSONParseException extends RuntimeException {

    /**
     * Constructs a new JSONParseException
     *
     * @param json the error parsed json
     */
    public JSONParseException(final String json) {
        super("Error in parsing JSON: " + json + ".");
    }
}
