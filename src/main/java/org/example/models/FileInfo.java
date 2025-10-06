package org.example.models;

public class FileInfo {
    public final String name;
    public final String path;
    public int downloads;
    public long uploadedAt;
    public long lastDownloadedAt;

    public FileInfo(String name, String path) {
        this.name = name;
        this.path = path;
        this.downloads = 0;
        this.uploadedAt = System.currentTimeMillis();
        this.lastDownloadedAt = 0;
    }

    public void incrementDownloads() {
        downloads++;
    }
}
