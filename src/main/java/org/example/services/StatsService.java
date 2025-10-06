//package org.example.services;
//
//import com.sun.net.httpserver.HttpExchange;
//import org.example.models.FileInfo;
//
//import java.io.IOException;
//import java.util.Collection;
//
//import static org.example.utils.HttpUtils.sendJson;
//
//public class StatsService {
//    private final FileService fileService;
//
//    public StatsService(FileService fileService) {
//        this.fileService = fileService;
//    }
//
//    public void handleStats(HttpExchange exchange) throws IOException {
//        Collection<FileInfo> files = fileService.getAllFiles();
//        StringBuilder sb = new StringBuilder("[");
//        for (FileInfo f : files) {
//            sb.append(String.format("{\"name\":\"%s\",\"downloads\":%d},", f.name, f.downloads));
//        }
//        if (sb.charAt(sb.length() - 1) == ',') sb.setLength(sb.length() - 1);
//        sb.append("]");
//
//        sendJson(exchange, 200, sb.toString());
//    }
//}
