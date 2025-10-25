# Customer Service

[![Java](https://img.shields.io/badge/Java-17%2B-blue?logo=java)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0%2B-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

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
