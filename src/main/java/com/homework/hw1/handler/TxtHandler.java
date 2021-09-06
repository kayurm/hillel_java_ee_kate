package com.homework.hw1.handler;

import com.homework.hw1.storage.StateMapSingleton;
import com.homework.hw1.util.ResponseHelper;
import com.homework.hw1.util.URIHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class TxtHandler implements HttpHandler {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private final StateMapSingleton stateMap;

    public TxtHandler() {
        stateMap = StateMapSingleton.getInstance();
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        logger.info("/txt endpoint handler");
        String reqMethod = httpExchange.getRequestMethod();
        switch (reqMethod) {
            case "GET":
                handleGetRequest(httpExchange);
                break;
            case "POST":
                handlePostRequest(httpExchange);
                break;
            case "DELETE":
                handleDeleteRequest(httpExchange);
                break;
        }
    }

    private void handleGetRequest(HttpExchange httpExchange) {
        URI uri = httpExchange.getRequestURI();
        if (URIHelper.isSingleKeyInUri(uri, "txt")) {
            doResponseForParsedFromUriValue(httpExchange);
        } else {
            doResponseAllValues(httpExchange);
        }
    }

    private void handlePostRequest(HttpExchange httpExchange) {
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String body = null;
        try {
            body = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        URI uri = httpExchange.getRequestURI();

        if (URIHelper.isSingleKeyInUri(uri, "txt")) {
            Integer key = URIHelper.getIdFromUri(httpExchange.getRequestURI());
            logger.info("Post: parsed key from the uri: " + key);
            if (stateMap.doesKeyExist(key) && body != null) {
                logger.info("Post: body not null");
                stateMap.modifyValue(key, body);
                doResponseByKey(httpExchange, key);
            } else {
                logger.info("Post: body is not null and the key is unknown");
                ResponseHelper.doBadRequestResponse(httpExchange);
            }
        } else if (body != null) {
            if (!stateMap.doesValueExist(body)) {
                logger.info("Post: new body");
                Integer key = stateMap.addValue(body);
                doResponseByKey(httpExchange, key);
            } else {
                logger.info("Post: existent body");
                doResponseForExistentBody(httpExchange, body);
            }
        } else {
            logger.info("Post: body is null");
            ResponseHelper.doBadRequestResponse(httpExchange);
        }
    }

    private void handleDeleteRequest(HttpExchange httpExchange) {
        URI uri = httpExchange.getRequestURI();
        if (URIHelper.isSingleKeyInUri(uri, "txt")) {
            logger.info("Delete: key in uri");
            Integer key = URIHelper.getIdFromUri(httpExchange.getRequestURI());
            stateMap.deleteValue(key);
            doResponseDeletedByKey(httpExchange, key);
        } else {
            logger.info("Delete: no key in uri");
            ResponseHelper.doBadRequestResponse(httpExchange);
        }
    }

    private void doResponseByKey(HttpExchange httpExchange, Integer key) {
        logger.info("preparing response for key: " + key);
        ResponseHelper.doResponse(httpExchange, stateMap.getMapValue(key));
    }

    private void doResponseForParsedFromUriValue(HttpExchange httpExchange) {
        Integer txtId = URIHelper.getIdFromUri(httpExchange.getRequestURI());
        String respValue = stateMap.getMapValue(txtId);
        ResponseHelper.doResponse(httpExchange, respValue);
    }

    private void doResponseAllValues(HttpExchange httpExchange) {
        String allValues = stateMap.getAllMapValuesAsString();
        String respValue = String.format("All txt values: %s", allValues);
        ResponseHelper.doResponse(httpExchange, respValue);
    }

    private void doResponseForExistentBody(HttpExchange httpExchange, String body) {
        Integer key = stateMap.getMapKey(body);
        String respValue = String.format("Such body already exists with the key(id): %d", key);
        ResponseHelper.doResponse(httpExchange, respValue);
    }

    private void doResponseDeletedByKey(HttpExchange httpExchange, Integer key) {
        String respValue = String.format("Corresponding value was deleted if key existed: %d", key);
        ResponseHelper.doResponse(httpExchange, respValue);
    }

}




