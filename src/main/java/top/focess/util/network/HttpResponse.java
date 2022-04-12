package top.focess.util.network;

import okhttp3.Headers;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.focess.util.json.JSON;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * This class is used to define a response to a http-request
 */
public class HttpResponse {

    /**
     * Unknown request type error code
     */
    public static final int UNKNOWN_REQUEST = -1;

    /**
     * Exception thrown error code
     */
    public static final int EXCEPTION = -2;

    /**
     * Unknown request type HttpResponse
     */
    private static final Supplier<HttpResponse> UNKNOWN_REQUEST_TYPE = HttpResponse::new;

    /**
     * The response code
     */
    private final int code;

    /**
     * The response headers
     */
    private Headers headers;
    /**
     * The response data
     */
    private String value;
    /**
     * The exception thrown in http-request processing
     */
    private Exception exception;

    /**
     * Initialize a HttpResponse with code
     *
     * @param code   the response code
     */
    private HttpResponse(final int code) {
        this.code = code;
    }

    private HttpResponse() {
        this(UNKNOWN_REQUEST);
    }

    /**
     * Initialize an exception thrown HttpResponse with e
     *
     * @param e      the thrown exception in this http-request processing
     */
    public HttpResponse(final Exception e) {
        this(EXCEPTION);
        this.exception = e;
    }

    /**
     * Initialize a HttpResponse without exceptions
     *
     * @param code    the response code
     * @param headers the response header
     * @param value   the response data
     */
    public HttpResponse(final int code, final Headers headers, final String value) {
        this(code);
        this.value = value;
        this.headers = headers;
    }

    public static HttpResponse ofNull() {
        return UNKNOWN_REQUEST_TYPE.get();
    }

    @Nullable
    public Exception getException() {
        return this.exception;
    }

    public int getCode() {
        return this.code;
    }

    /**
     * Get the value as JSON
     *
     * @return JSON instance of this response data
     * @throws HttpResponseException if there is something wrong with this request
     */
    @NotNull
    public JSON getAsJSON() throws HttpResponseException {
        if (this.isError())
            throw new HttpResponseException(Objects.requireNonNull(this.getException()));
        return new JSON(this.value);
    }

    /**
     * Get the value as String
     * @return String instance of this response data
     * @throws HttpResponseException if there is something wrong with this request
     */
    @NonNull
    public String getAsString() throws HttpResponseException {
        if (this.isError())
            throw new HttpResponseException(Objects.requireNonNull(this.getException()));
        return this.value;
    }

    @Nullable
    public String getResponse() {
        return this.value;
    }

    @Nullable
    public Headers getHeaders() {
        return this.headers;
    }

    /**
     * Indicate this is an exception thrown HttpResponse
     *
     * @return true if this is an exception thrown HttpResponse, false otherwise
     */
    @EnsuresNonNullIf(result = true, expression = "getException()")
    public boolean isError() {
        return this.code == EXCEPTION;
    }

}
