package org.example.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.example.controllers.AuthController;
import org.example.controllers.FileController;
import org.example.services.file.FileService;
import org.example.services.file.IFileService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {
    private final int port;
    private HttpServer server;
    private final AppContext context;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Server(int port) {
        this.port = port;
        this.context = AppConfig.init();
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        HttpHandler authController = context.get(AuthController.class);
        HttpHandler fileController = context.get(FileController.class);
        HandlerWrapper wrapper = context.get(HandlerWrapper.class);

        server.createContext("/login", wrapper.requirePost(authController::handle));
        server.createContext("/upload", wrapper.requirePostAndAuth(fileController::handle));
        server.createContext("/download", wrapper.requireGet(fileController::handle));

        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();

        System.out.println("✅ Server started on http://localhost:" + port);



        IFileService fileService = context.get(IFileService.class);
        scheduler.scheduleAtFixedRate(
                fileService::cleanupOldFiles,
                0,
                24,
                TimeUnit.HOURS
        );
    }
}
