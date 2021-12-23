package fr.lernejo.navy_battle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POSTHandler implements HttpHandler {
    private final UUID id = UUID.randomUUID();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("POST"))
            postContext(exchange);
        else
            notFoundContext(exchange);
    }

    private void postContext(HttpExchange exchange) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String body = gson.toJson(new RequestBody(id.toString(),exchange.getLocalAddress().toString(),"May the best Win!"));
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

    private boolean isFormatCorrect(String response) throws JsonSyntaxException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        RequestBody body = gson.fromJson(response,RequestBody.class);
        if(body.getMessage() == null || body.getId() == null || body.getClass() == null)
            return false;
        else
            return true;
    }
}
