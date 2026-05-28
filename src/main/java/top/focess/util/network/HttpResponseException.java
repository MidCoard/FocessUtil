package top.focess.util.network;

import java.io.IOException;

/**
 * Thrown to indicate that the request was not successful
 * @deprecated FocessUtil is no longer maintained. Do not use.
 */
@Deprecated(forRemoval = true, since = "1.1.25")
public class HttpResponseException extends IOException {

    /**
     * Constructs a HttpResponseException
     * @param exception the cause
     */
    public HttpResponseException(Exception exception) {
        super("This request was not successful.", exception);
    }
}
