package top.focess.util.network;

import java.util.Map;

public interface HttpHandler {

    void handle(String url, Map<String,Object> data, Map<String,String> header, String body);

    void handleException(String url, Map<String,Object> data, Map<String,String> header,Exception e);

}
