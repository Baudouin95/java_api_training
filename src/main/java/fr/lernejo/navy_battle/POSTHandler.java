package fr.lernejo.navy_battle;

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

    private String composeResponse(UUID id, InetSocketAddress url, String message) {
        return "\"id\": "+id+"\n\"url\": "+url.toString()+"\n\"message\": "+message;
    }

    private boolean isFormatCorrect(String response) {
        Pattern patternID = Pattern.compile("\"id\":\s\"[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}\",");
        Pattern patternURL = Pattern.compile("\"url\":\s\"(https?|ftp|ssh|mailto):\\/\\/[a-z0-9\\/:%_+.,#?!@&=-]+\",");
        Pattern patternMessage = Pattern.compile("\"message\":\s\"(.*)\"");
        Matcher matchID = patternID.matcher(response);
        Matcher matchURL = patternURL.matcher(response);
        Matcher matchMessage = patternMessage.matcher(response);
        if(matchURL.find() && matchID.find() && matchMessage.find())
            return true;
        else
            return false;
    }
}
