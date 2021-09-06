package com.homework.hw1.util;

import com.sun.net.httpserver.HttpExchange;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ResponseHelper {
    private static final Logger logger = LogManager.getLogger(ResponseHelper.class);

    public static void doResponse(HttpExchange httpExchange, String value){
        String resp = String.format("<html><body><h1>%s</h1></body></html>", value);

        try {
            httpExchange.sendResponseHeaders(200, resp.length());
            httpExchange.getResponseBody().write(resp.getBytes());
            httpExchange.close();
            logger.info(httpExchange.getRequestURI() + " processed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void doBadRequestResponse(HttpExchange httpExchange){
        String resp = "Bad request";
        try {
            httpExchange.sendResponseHeaders(400, resp.length());
            httpExchange.getResponseBody().write(resp.getBytes());
            httpExchange.close();
            logger.info(httpExchange.getRequestURI() + " processed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
