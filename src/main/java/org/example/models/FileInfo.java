package org.example.models;

import java.util.concurrent.atomic.AtomicInteger;

public class FileInfo {
    public final String name;
    public final String path;
    public final AtomicInteger downloads = new AtomicInteger(0);
    public long uploadedAt;
    public long lastDownloadedAt;

    public FileInfo(String name, String path) {
        this.name = name;
        this.path = path;
        this.uploadedAt = System.currentTimeMillis();
        this.lastDownloadedAt = 0;
    }

    public void incrementDownloads() {
        downloads.incrementAndGet();
    }

    public int getDownloads() {
        return downloads.get();
    }
}
