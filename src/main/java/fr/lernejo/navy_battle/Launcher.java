package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class Launcher {
    public static void main(String[] args) throws IOException {
            HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(args[0])), 0);
            server.setExecutor(Executors.newFixedThreadPool(1));
            server.createContext("/ping", new MyHttpHandler());
            server.start();
    }

    static class MyHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String body = "OK";
            exchange.sendResponseHeaders(200, body.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body.getBytes());
            }
        }
    }
}
