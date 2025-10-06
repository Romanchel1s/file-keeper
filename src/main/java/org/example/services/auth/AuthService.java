package org.example.services.auth;

import com.sun.net.httpserver.HttpExchange;
import org.example.models.LoginRequest;
import org.example.models.LoginResponse;


import java.util.*;
import java.util.Base64;


public class AuthService implements IAuthService {

    @Override
    public LoginResponse login(LoginRequest request) {
        if ("admin".equals(request.username()) && "password".equals(request.password())) {
            String token = createToken(request.username());
            return new LoginResponse(token);
        } else {
            return null;
        }
    }

    @Override
    public boolean isAuthorized(HttpExchange exchange) {
        List<String> auth = exchange.getRequestHeaders().get("Authorization");
        if (auth == null || auth.isEmpty()) return false;
        String token = auth.get(0).replace("Bearer ", "");
        return verifyToken(token);
    }

    private String createToken(String user) {
        String payload = user + ":" + (System.currentTimeMillis() + 3600_000);
        return Base64.getEncoder().encodeToString(payload.getBytes());
    }

    private boolean verifyToken(String token) {
        try {
            String payload = new String(Base64.getDecoder().decode(token));
            long expiry = Long.parseLong(payload.split(":")[1]);
            return expiry > System.currentTimeMillis();
        } catch (Exception e) {
            return false;
        }
    }
}