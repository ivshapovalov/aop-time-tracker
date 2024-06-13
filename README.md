# aop-time-tracker

Сервис учета времени выполнения методов ([Задача](/docs/task.txt))

## Docker запуск

Ранее созданные контейнеры
```
docker compose up
```
Пересоздать все контейнеры полностью
```
docker compose build --no-cache && docker compose up --force-recreate --remove-orphans
```

### Запуск
Для разработки используется профиль Spring со следующими свойствами
```
    url: jdbc:postgresql://localhost:5432/time_tracker
    username: postgres
    password: postgres
```
- Необходимо установить Postgresql локально, или через докер
```
docker run --name aop-time-tracker-db -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres
```
 - Создать базу данных time_tracker (через консоль докера, в IDEA, через pgAdmin)
 - Затем запустить приложение 
```
mvn spring-boot:run pom.xml
```

### Использование swagger-ui
 - api http://localhost:8080/v3/api-docs
 - swagger-ui http://localhost:8080/swagger-ui/index.html

При успешной работе swagger должен отобразиться графический интерфейс swagger-ui со списком запросов.

### Описание работы
- Запускаем
    - http://localhost:8080/generate- случайное выполнение нескольких методов нескольких сервисов
- Затем
  - http://localhost:8080/statistics - вывод статистики


