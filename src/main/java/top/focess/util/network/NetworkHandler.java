package top.focess.util.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import okhttp3.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.focess.util.json.JSON;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * This is a network util class.
 */
public class NetworkHandler {

    /**
     * Used to indicate this http-request accepts JSON
     */
    @NonNull
    public static final MediaType JSON = Objects.requireNonNull(MediaType.parse("application/json; charset=utf-8"));
    /**
     * Used to indicate this http-request accepts normal String
     */
    @NonNull
    public static final MediaType TEXT = Objects.requireNonNull(MediaType.parse("text/plain; charset=utf-8"));
    @NonNull
    public static final MediaType URL_ENCODED = Objects.requireNonNull(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"));
    private static final X509TrustManager[] X_509_TRUST_MANAGERS = {
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s) throws CertificateNotYetValidException, CertificateExpiredException {
                    //check if the certificate is valid
                    for (final X509Certificate certificate : x509Certificates)
                        certificate.checkValidity();
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s) throws CertificateNotYetValidException, CertificateExpiredException {
                    //check if the certificate is valid
                    for (final X509Certificate certificate : x509Certificates)
                        certificate.checkValidity();
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
    };
    private static final SSLContext SSL_CONTEXT;

    static {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, X_509_TRUST_MANAGERS, new SecureRandom());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        SSL_CONTEXT = sslContext;
    }
    private final OkHttpClient client;
    private final List<HttpHandler> handlers = Lists.newArrayList();

    /**
     * Initialize a NetworkHandler with specified options
     * @param options the options
     */
    public NetworkHandler(Options options) {
        this.client = new OkHttpClient.Builder().connectTimeout(options.connectTimeout, TimeUnit.SECONDS).writeTimeout(options.writeTimeout, TimeUnit.SECONDS).readTimeout(options.readTimeout, TimeUnit.SECONDS).sslSocketFactory(SSL_CONTEXT.getSocketFactory(), X_509_TRUST_MANAGERS[0]).hostnameVerifier((hostname, session) -> true).build();
    }


    /**
     * Initialize a NetworkHandler with default options
     */
    public NetworkHandler() {
        this(Options.ofNull());
    }

    /**
     * Send a http-request
     *
     * @param url         the request url
     * @param data        the request data
     * @param requestType the request type
     * @return the response of this request
     */
    public HttpResponse request(final String url, final Map<String, Object> data, final RequestType requestType) {
        return this.request(url, data, Maps.newHashMap(), TEXT, requestType);
    }

    /**
     * Send a http-request
     *
     * @param url         the request url
     * @param requestType the request type
     * @return the response of this request
     * @see NetworkHandler#request(String, Map, RequestType)
     */
    public HttpResponse request(final String url, final RequestType requestType) {
        return this.request(url, Maps.newHashMap(), requestType);
    }

    /**
     * Send a http-request
     *
     * @param url         the request url
     * @param data        the request data
     * @param header      the request header
     * @param mediaType   the request acceptable type
     * @param requestType the request type
     * @return the response of this request
     */
    public HttpResponse request(final String url, final Map<String, Object> data, final Map<String, String> header, final MediaType mediaType, final RequestType requestType) {
        if (requestType == RequestType.GET)
            return this.get(url, Collections.emptyMap(), header);
        else
            return this.request(url, mediaType == NetworkHandler.JSON ? new JSON(data).toJson() : this.process(data), header, mediaType, requestType);
    }

    /**
     * Send a http-request
     *
     * @param url         the request url
     * @param data        the request data
     * @param header      the request header
     * @param mediaType   the request acceptable type
     * @param requestType the request type
     * @return the response of this request
     */
    public HttpResponse request(final String url, final String data, final Map<String, String> header, final MediaType mediaType, final RequestType requestType) {
        if (requestType == RequestType.GET)
            return this.get(url, Collections.emptyMap(), header);
        else if (requestType == RequestType.POST)
            return this.post(url, data, header, mediaType);
        else if (requestType == RequestType.PUT)
            return this.put(url, data, header, mediaType);
        else if (requestType == RequestType.DELETE)
            return this.delete(url, data, header, mediaType);
        return HttpResponse.ofNull();
    }

    private String process(@NotNull final Map<String, Object> data) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final String key : data.keySet())
            stringBuilder.append(key).append('=').append(data.get(key)).append('&');
        if (stringBuilder.length() != 0)
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        return "";
    }

    /**
     * Send a PUT http-request
     *
     * @param url       the request url
     * @param data      the request data
     * @param header    the request header
     * @param mediaType the request acceptable type
     * @return the response of this request
     */
    public HttpResponse put(final String url, final String data, final Map<String, String> header, @NotNull final MediaType mediaType) {
        final RequestBody requestBody = RequestBody.create(data, mediaType);
        final Request request = new Request.Builder().url(url).headers(Headers.of(header)).put(requestBody).build();
        return this.sendHttpRequest(url, data, header, request);
    }

    /**
     * Send a POST http-request
     *
     * @param url       the request url
     * @param data      the request data
     * @param header    the request header
     * @param mediaType the request acceptable type
     * @return the response of this request
     */
    public HttpResponse post(final String url, final String data, final Map<String, String> header, @NotNull final MediaType mediaType) {
        final RequestBody requestBody = RequestBody.create(data, mediaType);
        final Request request = new Request.Builder().url(url).headers(Headers.of(header)).post(requestBody).build();
        return this.sendHttpRequest(url, data, header, request);
    }

    /**
     * Send a DELETE http-request
     *
     * @param url       the request url
     * @param data      the request data
     * @param header    the request header
     * @param mediaType the request acceptable type
     * @return the response of this request
     */
    public HttpResponse delete(final String url, final String data, final Map<String, String> header, @NotNull final MediaType mediaType) {
        final RequestBody requestBody = RequestBody.create(data, mediaType);
        final Request request = new Request.Builder().url(url).headers(Headers.of(header)).delete(requestBody).build();
        return this.sendHttpRequest(url, data, header, request);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    private HttpResponse sendHttpRequest(final String url, final String data, final Map<String, String> header, final Request request) {
        try {
            final Response response = client.newCall(request).execute();
            // Call#execute() returns a non-null Response object
            final String body = Objects.requireNonNull(response.body()).string();
            this.handlers.forEach(handler -> handler.handle(url,data,header,body));
            return new HttpResponse( response.code(), response.headers(), body);
        } catch (final Exception e) {
            this.handlers.forEach(handler -> handler.handleException(url,data,header,e));
            return new HttpResponse( e);
        }
    }

    /**
     * Send a GET http-request
     *
     * @param url    the request url
     * @param data   the request data
     * @param header the request header
     * @return the response of this request
     */
    public HttpResponse get(final String url, @NotNull final Map<String, Object> data, final Map<String, String> header) {
        final Request request;
        if (!data.isEmpty())
            request = new Request.Builder().url(url + "?" + this.process(data)).get().headers(Headers.of(header)).build();
        else
            request = new Request.Builder().url(url).get().headers(Headers.of(header)).build();
        return this.sendHttpRequest(url + "?" + this.process(data), "", header, request);
    }

    /**
     * Add a http handler to this network handler
     *
     * @param handler the http handler
     */
    public void addHandler(final HttpHandler handler) {
        this.handlers.add(handler);
    }

    /**
     * Represents a request-type
     */
    public enum RequestType {
        /**
         * HTTP GET Request Method
         */
        GET,
        /**
         * HTTP POST Request Method
         */
        POST,
        /**
         * HTTP PUT Request Method
         */
        PUT,
        /**
         * HTTP DELETE Request Method
         */
        DELETE
    }

    /**
     * NetworkHandler Options
     */
    public static class Options {

        private int connectTimeout = 5;
        private int writeTimeout = 10;
        private int readTimeout = 10;

        /**
         * Set the connecting timeout
         * @param connectTimeout the connecting timeout
         * @return the configured options
         */
        public static Options ofConnectTimeout(final int connectTimeout) {
            return new Options().setConnectTimeout(connectTimeout);
        }

        /**
         * Set the writing timeout
         *
         * @param writeTimeout the writing timeout
         * @return the configured options
         */
        public static Options ofWriteTimeout(final int writeTimeout) {
            return new Options().setWriteTimeout(writeTimeout);
        }

        /**
         * Set the reading timeout
         * @param readTimeout the reading timeout
         * @return the configured options
         */
        public static Options ofReadTimeout(final int readTimeout) {
            return new Options().setReadTimeout(readTimeout);
        }

        /**
         * Use the default options
         * @return the default options
         */
        public static Options ofNull() {
            return new Options();
        }

        /**
         * Set the connecting timeout
         * @param connectTimeout the connecting timeout
         * @return itself
         */
        public Options setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        /**
         * Set the reading timeout
         * @param readTimeout the reading timeout
         * @return itself
         */
        public Options setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        /**
         * Set the writing timeout
         * @param writeTimeout the writing timeout
         * @return itself
         */
        public Options setWriteTimeout(int writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }
    }

}
