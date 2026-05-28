package top.focess.util.network;

import java.util.Map;

/**
 * @deprecated FocessUtil is no longer maintained. Do not use.
 */
@Deprecated(forRemoval = true, since = "1.1.25")
public interface HttpHandler {

    void handle(String url, String data, Map<String,String> header, String body);

    void handleException(String url, String data, Map<String,String> header,Exception e);

}
