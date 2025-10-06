let authToken = null;
const content = document.getElementById('content');
const errorDiv = document.getElementById('error');

// --- Логин ---
async function login(username, password) {
  const resp = await fetch('http://localhost:8080/login', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({username, password})
  });

  if (!resp.ok) throw new Error('Неверный логин или пароль');
  const data = await resp.json();
  authToken = data.token;
}

// --- Загрузка файла ---
function uploadFile(file, onProgress) {
  return new Promise((resolve, reject) => {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://localhost:8080/upload');
    xhr.setRequestHeader('Authorization', 'Bearer ' + authToken);

    xhr.upload.onprogress = onProgress;

    xhr.onload = () => {
      if (xhr.status === 200) resolve(JSON.parse(xhr.responseText));
      else reject(new Error('Ошибка загрузки: ' + xhr.statusText));
    };
    xhr.onerror = () => reject(new Error('Ошибка сети'));

    const formData = new FormData();
    formData.append('file', file);
    xhr.send(formData);
  });
}

// --- Получение статистики ---
async function fetchStats() {
  const resp = await fetch('http://localhost:8080/stats');
  if (!resp.ok) throw new Error('Не удалось получить статистику');
  return await resp.json();
}

// --- Обработка формы логина ---
const loginForm = document.getElementById('loginForm');
loginForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;

  try {
    await login(username, password);
    errorDiv.textContent = '';
    showUploadForm();
  } catch (err) {
    errorDiv.textContent = err.message;
  }
});

// --- Отображение формы загрузки ---
function showUploadForm() {
  content.innerHTML = `
    <form id="uploadForm">
      <input type="file" id="fileInput" required>
      <button type="submit">Загрузить</button>
      <div class="progress">
        <div class="progress-bar" id="progressBar"></div>
      </div>
    </form>
    <div class="link-container" id="linkContainer"></div>
    <button id="statsButton">Показать статистику</button>
    <div class="stats-card" id="statsOutput"></div>
    <div class="error" id="uploadError"></div>
  `;

  const form = document.getElementById('uploadForm');
  const progressBar = document.getElementById('progressBar');
  const linkContainer = document.getElementById('linkContainer');
  const uploadError = document.getElementById('uploadError');
  const statsButton = document.getElementById('statsButton');
  const statsOutput = document.getElementById('statsOutput');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const file = document.getElementById('fileInput').files[0];
    if (!file) return;

    try {
      const result = await uploadFile(file, (event) => {
        if (event.lengthComputable) {
          const percent = (event.loaded / event.total) * 100;
          progressBar.style.width = percent + '%';
        }
      });

      linkContainer.innerHTML = `<p>Ссылка для скачивания (действует 1 минуту):</p>
        <a href="${result.link}" target="_blank">${result.link}</a>`;
      uploadError.textContent = '';
    } catch (err) {
      uploadError.textContent = err.message;
    }
  });

  statsButton.addEventListener('click', async () => {
    try {
      const stats = await fetchStats();
      statsOutput.textContent = JSON.stringify(stats, null, 2);
    } catch (err) {
      statsOutput.textContent = err.message;
    }
  });
}
