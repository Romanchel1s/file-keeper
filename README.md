# FileKeeper

**FileKeeper** — простой HTTP-сервер на Java для загрузки, скачивания и статистики файлов с авторизацией.

---

## 📦 Функционал

- Авторизация через `POST /login`
- Загрузка файлов через `POST /upload` (требуется авторизация)
- Скачивание файлов через `GET /download?id=FILENAME&token=TOKEN`
- Авто-удаление файлов, которые не скачивались более 30 дней
- CORS поддержка (GET, POST, OPTIONS)
- Все файлы хранятся на диске в папке `uploads`

---
## 🌐 Фронтенд
В проекте есть папка front, которая содержит:
* index.html — основная страница приложения
* style.css — стили для красивого отображения формы логина, загрузки файлов и статистики
* script.js — логика для авторизации, загрузки файлов и получения статистики

### ⚡ Как использовать:

1. Откройте index.html в браузере.

2. Введите данные для логина:
```
Username: admin
Password: password
```
3. После успешного логина появится форма для загрузки файла и кнопка для получения статистики.
---
## ⚡ Примеры запросов
Авторизация:
```bash
curl -X POST http://localhost:8080/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"password"}'

```
Загрузка файла:
```bash
curl -X POST http://localhost:8080/upload \
     -H "Authorization: Bearer <TOKEN>" \
     --data-binary "@myfile.txt"
```
Статистика скачиваний файлов:
```bash
curl -X GET http://localhost:8080/stats
```
Скачивание файла:
```bash
curl -X GET "http://localhost:8080/download?id=file_123" -O
```
---

## ⚙️ Структура проекта

src/

└─ main/

└─ java/

└─ org/example/

├─ server/ # HTTP сервер и wrapper

├─ controllers/ # Контроллеры для обработки запросов

├─ services/ # Логика приложения (AuthService, FileService)

├─ models/ # DTO

└─ utils/ # Вспомогательные методы (HttpUtils)


---

## 🛠 Зависимости

- JDK 17+
- Jackson Databind для JSON сериализации
- JUnit 5 для тестирования

---

## 🚀 Запуск проекта через Gradle
1. Склонировать репозиторий и перейти в директорию проекта:
```bash
git clone <repo-url>
cd FileKeeper
```
2. Сборка проекта:
```bash
./gradlew build
```
2. Запуск сервера:
```bash
./gradlew run
```
По умолчанию сервер будет доступен по адресу: http://localhost:8080
