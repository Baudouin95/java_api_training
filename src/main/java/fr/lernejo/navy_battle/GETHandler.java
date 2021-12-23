package fr.lernejo.navy_battle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class GETHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().equals("GET"))
            getContext(exchange);
        else
            notFoundContext(exchange);
    }

    private void getContext(HttpExchange exchange) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        

    }

    private void notFoundContext(HttpExchange exchange) throws IOException {
        String body = "Error 404 Not Found";
        exchange.sendResponseHeaders(404,body.length());
        try (OutputStream os = exchange.getResponseBody()){
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
        FireRequest body = gson.fromJson(response,FireRequest.class);
        if(body.getConsequence() == null)
            return false;
        else
            return true;
    }
}
