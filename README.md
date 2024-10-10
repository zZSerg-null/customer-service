# USER-SERVICE

### Установка Docker и запуск контейнера PostgreSQL (ОС Windows)

- Скачать и установить Docker-desktop: https://www.docker.com/products/docker-desktop/
- В окне **Docker Desktop** в заголовке в поиске (Ctrl+K) найти образ `postgres`. Выбрать версию `latest`, нажать
  **Pull**
- Перейти в раздел Images (слева в меню). Найти там скачанный образ postgres и нажать напротив него **Run**
- В окне **Run a new container** раскрыть раздел **Optional settings**, заполнить поля:
    - **Container name**: произвольное имя, например `zinoviev_bank_postgres`
    - **Host port**: `5432` - порт, который будет прокидываться в контейнер (если на компьютере уже установлен PostgreSQL
      сервер, то можно выбрать другой порт, чтобы они не конфликтовали)
    - **Environment variables**: обязательно нужно добавить переменную окружения `POSTGRES_PASSWORD`, значение - пароль
      для подключения к БД, например `password`
    - Нажать **Run**. Окно Docker Desktop можно закрыть

### Установка Docker и запуск контейнера PostgreSQL (Ubuntu)

- `docker compose up` - запускает докер контейнер
- `sudo apt install docker.io -y`
- `docker pull postgres`
- `docker run -it --name zinoviev_bank_postgres --publish 127.0.0.1:5432:5432/tcp --env POSTGRES_PASSWORD=password
  postgres`

### Подключение к серверу PostgreSQL

Можно подключиться через pgAdmin, или в Intellij Idea (только в Ultimate) стандартным клиентом БД. Для подключения
использовать параметры:

- **Host**: `localhost`
- **Port**: `5432` (если не менялся на свой)
- **User**: `postgres`
- **Password**: `postgres` (пароль, который задавался в переменной POSTGRES_PASSWORD)
- **Database**: если подключаемся первый раз, и своя БД на сервере еще не создана, то можно не указывать
