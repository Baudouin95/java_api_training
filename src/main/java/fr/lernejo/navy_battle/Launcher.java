package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Launcher {
    public static void main(String[] args) throws IOException {
            HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(args[0])), 0);
            server.setExecutor(Executors.newFixedThreadPool(1));
            server.createContext("/ping", new MyHttpHandler());
            server.createContext("/api/game/start", new POSTHandler());
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

    static class POSTHandler implements HttpHandler {
        private final UUID id = UUID.randomUUID();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            switch (exchange.getRequestMethod()) {
                case "POST" -> postContext(exchange);
                case "GET" -> getContext(exchange);
                default -> notFoundContext(exchange);
            }
        }

        private void postContext(HttpExchange exchange) throws IOException {
            String body = composeResponse(id,exchange.getLocalAddress(), "May the best win");
            StringBuilder sb = new StringBuilder();
            for (int ch; (ch = exchange.getRequestBody().read()) != -1; )
                sb.append((char) ch);
            if(!isFormatCorrect(sb.toString()))
                badRequest(exchange);
            else {
                exchange.sendResponseHeaders(202,body.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body.getBytes());
                }
            }
        }

        private void getContext(HttpExchange exchange) {

        }

        private void notFoundContext(HttpExchange exchange) throws IOException {
            String body = "Error 404 NOT FOUND";
            exchange.sendResponseHeaders(404,body.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body.getBytes());
            }
        }

        private void badRequest(HttpExchange exchange) throws IOException {
            String body = "Bad Request";
            exchange.sendResponseHeaders(400,body.length());
            try (OutputStream os = exchange.getResponseBody()){
                os.write(body.getBytes());

            }
        }

        private String composeResponse(UUID id,InetSocketAddress url,String message) {
            return "\"id\": "+id+"\n\"url\": "+url.toString()+"\n\"message\": "+message;
        }

        private boolean isFormatCorrect(String response) {
            Pattern patternID = Pattern.compile("\"id\": \"[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}\",");
            Pattern patternURL = Pattern.compile("\"url\": \"(https?|ftp|ssh|mailto):\\/\\/[a-z0-9\\/:%_+.,#?!@&=-]+\",");
            Pattern patternMessage = Pattern.compile("\"message\": \"(.*)\"");
            Matcher matchID = patternID.matcher(response);
            Matcher matchURL = patternURL.matcher(response);
            Matcher matchMessage = patternMessage.matcher(response);
            if(matchURL.find() && matchID.find() && matchMessage.find())
                return true;
            else
                return false;
        }
    }
}
