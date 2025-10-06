package org.example.services.auth;

import com.sun.net.httpserver.HttpExchange;
import org.example.models.LoginRequest;
import org.example.models.LoginResponse;

import java.io.IOException;

public interface IAuthService {
    /**
     * Выполняет авторизацию пользователя по логину и паролю.
     * @param request данные для логина
     * @return LoginResponse с токеном, или null если авторизация не удалась
     * @throws IOException при ошибке ввода-вывода
     */
    LoginResponse login(LoginRequest request) throws IOException;

    /**
     * Проверяет, авторизован ли запрос по заголовку Authorization.
     * @param exchange объект HttpExchange
     * @return true, если авторизован, иначе false
     */
    boolean isAuthorized(HttpExchange exchange);
}
