package org.example.services.file;

import org.example.models.FileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

public interface IFileService {

    /**
     * Загружает файл на сервер.
     * @param input поток с данными файла
     * @return идентификатор файла, который можно использовать для скачивания
     * @throws IOException при ошибке записи файла
     */
    String uploadFile(InputStream input) throws IOException;

    /**
     * Получает путь к файлу по идентификатору.
     * @param id идентификатор файла
     * @param token токен (если используется, иначе можно игнорировать)
     * @return Path к файлу, или null если файл не найден
     */
    Path getFileIfValid(String id);

    /**
     * Возвращает статистику скачиваний всех файлов.
     * @return Map где ключ — имя файла, значение — количество скачиваний
     */
    Map<String, Integer> getDownloadStats();

    /**
     * Удаляет устаревшие файлы (например, старше 30 дней).
     */
    void cleanupOldFiles();
}
