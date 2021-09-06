package com.homework.hw1;

import com.homework.hw1.handler.*;
import com.sun.net.httpserver.HttpServer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;


public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        int port = Integer.parseInt(System.getProperty("port", "80"));

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        logger.info("Server started on port: " + port);

        httpServer.createContext("/app", new RootHandler());
        httpServer.createContext("/app/txt", new TxtHandler());
        httpServer.setExecutor(null);
        httpServer.start();
    }
}
