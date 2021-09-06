package com.homework.hw1.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RootHandler implements HttpHandler {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public void handle(HttpExchange httpExchange) {
        try {
            String query1 = httpExchange.getRequestURI().getQuery();
            Map<String, String> requestParams = Optional.ofNullable(query1).stream()
                    .flatMap(query -> Arrays.stream(query.split("&")))
                    .filter(param -> param.indexOf("=") > 0)
                    .collect(Collectors.toMap(
                            param -> param.substring(0, param.indexOf("=")),
                            param -> param.substring(param.indexOf("=") + 1)
                    ));

            String recipient = requestParams.getOrDefault("name", "world");
            String resp = String.format("Hello, %s!", recipient);
            String s = String.format("<html><body><h1>%s</h1></body></html>", resp);

            httpExchange.sendResponseHeaders(200, s.length());
            httpExchange.getResponseBody().write(s.getBytes());
            httpExchange.close();
            logger.info(httpExchange.getRequestURI() + " processed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

