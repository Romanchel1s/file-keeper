package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.models.UploadResponse;
import org.example.services.file.FileService;
import org.example.services.file.IFileService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static org.example.utils.HttpUtils.*;

public class FileController implements HttpHandler {
    private final IFileService fileService;

    private final ObjectMapper mapper = new ObjectMapper();

    public FileController(IFileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        try {
            if ("/upload".equals(path)) {
                handleUpload(exchange);
            } else if ("/download".equals(path)) {
                handleDownload(exchange);
            } else if ("/stats".equals(path)) {
                handleGetStats(exchange);
            } else {
                sendJson(exchange, 404, "{\"error\":\"Not found\"}");
            }
        } catch (Exception e) {
            sendInternalError(exchange, e);
        }
    }

    private void handleUpload(HttpExchange exchange) throws IOException {
        String link = fileService.uploadFile(exchange.getRequestBody());
        String json = mapper.writeValueAsString(new UploadResponse(link));
        sendJson(exchange, 200, json);
    }

    private void handleGetStats(HttpExchange exchange) throws IOException {
        try {
            Map<String, Integer> stats = fileService.getDownloadStats();
            String json = mapper.writeValueAsString(stats);
            sendJson(exchange, 200, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            sendJson(exchange, 500, "{\"error\":\"Failed to serialize stats\"}");
        }
    }

    private void handleDownload(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
        String id = params.get("id");

        if (id == null) {
            sendJson(exchange, 400, "{\"error\":\"Missing parameters\"}");
            return;
        }

        Path file = fileService.getFileIfValid(id);
        if (file == null) {
            sendJson(exchange, 404, "{\"error\":\"File not found or token expired\"}");
            return;
        }

        sendFile(exchange, file);
    }
}
