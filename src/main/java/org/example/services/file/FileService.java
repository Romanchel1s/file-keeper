package org.example.services.file;

import org.example.models.FileInfo;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class FileService implements IFileService {
    private final Map<String, FileInfo> files = new ConcurrentHashMap<>();


    @Override
    public String uploadFile(InputStream input) throws IOException {
        String filename = saveFile(input);
        return generateDownloadLink(filename);
    }

    @Override
    public Path getFileIfValid(String id, String token) {
        FileInfo info = files.get(id);
        if (info != null) {
            info.lastDownloadedAt = System.currentTimeMillis();
            return Path.of(info.path);
        }
        return null;
    }

    @Override
    public void cleanupOldFiles() {
        long now = System.currentTimeMillis();
        long THIRTY_DAYS = 30L * 24 * 60 * 60 * 1000;

        Iterator<Map.Entry<String, FileInfo>> iterator = files.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, FileInfo> entry = iterator.next();
            FileInfo info = entry.getValue();
            long lastAccess = info.lastDownloadedAt > 0 ? info.lastDownloadedAt : info.uploadedAt;

            if (now - lastAccess > THIRTY_DAYS) {
                try {
                    Files.deleteIfExists(Path.of(info.path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iterator.remove();
            }
        }
    }

    private String saveFile(InputStream input) throws IOException {
        Files.createDirectories(Path.of("uploads"));
        String name = "file_" + UUID.randomUUID();
        Path path = Path.of("uploads", name);

        try (OutputStream out = Files.newOutputStream(path)) {
            input.transferTo(out);
        }

        FileInfo info = new FileInfo(name, path.toString());
        files.put(name, info);

        return name;
    }

    private String generateDownloadLink(String filename) {
        return "http://localhost:8080/download?id=" + filename;
    }

}
