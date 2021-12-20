package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Launcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(args[0])), 0);
        server.setExecutor(Executors.newFixedThreadPool(1));
        server.createContext("/ping", new MyHttpHandler());
        server.createContext("/api/game/start", new POSTHandler());
        server.start();

        if (args.length > 1 && args[1].matches("(https?|ftp|ssh|mailto):\\/\\/[a-z0-9\\/:%_+.,#?!@&=-]+")) {
            UUID id = UUID.randomUUID();
            String body = "\"id\": \"0c575465-21f6-43c9-8a2d-bc64c3ae6241\", \"url\": \"http://localhost:8795\", \"message\": \"I will crush you!\"";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(args[1] + "/api/game/start"))
                .setHeader("Accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }
    }
}
