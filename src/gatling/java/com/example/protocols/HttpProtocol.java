package com.example.protocols;

import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

public class HttpProtocol {

    public static HttpProtocolBuilder httpConf = http.baseUrl("http://localhost:3001")
            .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36")
            //.contentTypeHeader("application/json; charset=utf-8")
            .acceptHeader("application/json")
            .acceptCharsetHeader("utf-8")
            .contentTypeHeader("application/json; charset=utf8")
            .disableWarmUp()
            .disableCaching();

}
