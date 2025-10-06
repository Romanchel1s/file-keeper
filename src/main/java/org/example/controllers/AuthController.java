package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.models.LoginRequest;
import org.example.services.auth.IAuthService;

import java.io.IOException;

import static org.example.utils.HttpUtils.sendInternalError;
import static org.example.utils.HttpUtils.sendJson;

public class AuthController implements HttpHandler {
    private final IAuthService authService;
    private final ObjectMapper mapper = new ObjectMapper();

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            LoginRequest req;
            try {
                req = mapper.readValue(exchange.getRequestBody(), LoginRequest.class);
            } catch (JsonProcessingException e) {
                sendJson(exchange, 400, "{\"error\":\"Invalid JSON\"}");
                return;
            }
            var result = authService.login(req);

            if (result == null) {
                sendJson(exchange, 403, "{\"error\":\"Invalid credentials\"}");
                return;
            }

            String json = mapper.writeValueAsString(result);
            sendJson(exchange, 200, json);

        } catch (Exception e) {
            sendInternalError(exchange, e);
        }
    }
}
