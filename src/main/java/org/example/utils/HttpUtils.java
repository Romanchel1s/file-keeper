package org.example.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    public static void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        try {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } finally {
            exchange.close();
        }
    }

    public static void sendFile(HttpExchange exchange, Path path) throws IOException {
        try {
            exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
            exchange.sendResponseHeaders(200, Files.size(path));
            try (OutputStream os = exchange.getResponseBody()) {
                Files.copy(path, os);
            }
        } finally {
            exchange.close();
        }
    }


    public static void sendInternalError(HttpExchange exchange, Exception e) {
        try {
            e.printStackTrace(); // лог для консоли
            String json = "{\"error\":\"Internal server error\"}";
            sendJson(exchange, 500, json);
        } catch (IOException ioException) {
            // fallback — в случае критического сбоя при отправке ответа
            ioException.printStackTrace();
        }
    }

    public static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null) return map;
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2) map.put(kv[0], kv[1]);
        }
        return map;
    }
}
