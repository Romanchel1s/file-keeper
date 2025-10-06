package org.example.server;

import org.example.controllers.AuthController;
import org.example.controllers.FileController;
import org.example.services.auth.AuthService;
import org.example.services.auth.IAuthService;
import org.example.services.file.FileService;
import org.example.services.file.IFileService;

public class AppConfig {

    public static AppContext init() {
        AppContext context = new AppContext();

        // Регистрируем зависимости
        context.register(IAuthService.class, AuthService::new);
        context.register(IFileService.class, FileService::new);
        context.register(AuthController.class, () -> new AuthController(context.get(IAuthService.class)));
        context.register(FileController.class, () -> new FileController(context.get(IFileService.class)));
        context.register(HandlerWrapper.class, () -> new HandlerWrapper(context.get(IAuthService.class)));

        return context;
    }
}
