package top.focess.util.network;

import java.util.Map;

public interface HttpHandler {

    void handle(String url, String data, Map<String,String> header, String body);

    void handleException(String url, String data, Map<String,String> header,Exception e);

}
