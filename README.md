# Customer Service

Сервис **Customer Service** — микросервис, реализующий управление сущностью «Клиент (Customer)»: создание, чтение, обновление, удаление, а также публикацию событий в Apache Kafka.  
Реализован на Java + Spring Boot, построен по стандартам для личных целей

## 🚀 Ключевые особенности

- REST API для операций CRUD над сущностью Customer.  
- Интеграция с Apache Kafka — сервис публикует событие при создании/обновлении клиента.  
- Использование Spring Boot, Spring Data (JPA/Hibernate), PostgreSQL либо другой СУБД.  
- Контейнеризация через Docker + docker-compose для локального запуска.  
- CI/CD (Jenkinsfile) для сборки, тестирования и деплоя.  
- Конфигурация через Spring Profiles (например, `dev`, `prod`).  
- Стартовая архитектура, легко расширяемая в микросервисную экосистему.

.
## 📁 Структура репозитория
├── Dockerfile
├── docker-compose.yaml
├── Jenkinsfile
├── build.gradle.kts ← сборка проекта (Gradle Kotlin DSL)
├── settings.gradle.kts
├── gradle/… ← wrapper
├── src/
│ ├── main/java/… ← исходный код приложения
│ ├── main/resources/… ← application-properties, миграции, etc.
│ └── test/java/… ← модульные/интеграционные тесты
└── .gitignore

## 🛠 Быстрый старт

### Предварительные условия

- Docker & Docker Compose  
- Java 17+ (или версия, указанная в `build.gradle.kts`)  
- Доступ к Kafka и СУБД (или запуск их через `docker-compose.yaml`)  

### Запуск локально

1. Склонируйте репозиторий:
   ```bash
   git clone https://github.com/zZSerg-null/customer-service.git
   cd customer-service

2. Поднимите инфраструктуру (PostgreSQL + Kafka) через Docker Compose:
docker-compose up --build

3. Сборка и запуск приложения:
    ./gradlew bootRun

После старта сервис будет доступен по адресу http://localhost:8080 (если стандартный порт).
Проверьте эндпоинты, например: GET /api/customers.

Создание клиента через API (пример curl)

curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com"
      }'

🧪 Тестирование

    Запускаются модульные и интеграционные тесты:
    ./gradlew test


📦 Сборка артефакта
./gradlew clean bootJar

В результате будет создан файл build/libs/customer-service-<version>.jar, готовый к запуску или деплою.
🛳 CI/CD

В корне репозитория находится Jenkinsfile, задающий конвейер:
    сборка → тестирование → статический анализ → упаковка → публикация образа Docker → деплой в staging/production

Можно интегрировать SonarQube, Nexus/Artifactory, Kubernetes, Prometheus/Grafana.

🔧 Конфигурация
Настройки находятся в src/main/resources/application-*.yaml (или .properties). Основные параметры:
    spring.datasource.* — параметры подключения к базе данных
    spring.kafka.* — настройки Kafka (bootstrap-servers, topic)
    server.port — порт приложения
    logging.level.* — уровень логирования

🧱 Архитектурные аспекты
    Слой контроллеров (REST)
    Сервисный слой — бизнес-логика
    Репозиторный слой — работа с БД (Spring Data JPA)
    Асинхронная коммуникация через Kafka
    
    Варианты расширения:
        добавить слой «домен» (с доменными моделями, событиями) при переходе к Чистой архитектуре
        внедрить CQRS/Event Sourcing
        масштабировать через Kubernetes

✅ Дальнейшие задачи / планы
    Добавить подробную документацию API (Swagger/OpenAPI)
    Настроить мониторинг и алертинг (Prometheus + Grafana + ELK)
    Реализовать «soft delete» клиентов
    Добавить возможности фильтрации/поиска клиентов
    Поддержка версионирования API
    Подготовка образов Docker для production, multi-stage сборка
    Развёртывание в Kubernetes + Helm чарты
