# Calories Management

A full-stack Java Enterprise web application for tracking daily calorie intake. Users can manage their meals, set daily calorie targets, and get visual feedback on whether they've stayed within their limits. Admins can manage all users via a dedicated interface.

## Live Demo

[calories-management-iixw.onrender.com](https://calories-management-iixw.onrender.com/)

> **Demo credentials**
> | Role  | Email              | Password |
> |-------|--------------------|----------|
> | User  | user@yandex.ru     | password |
> | Admin | admin@gmail.com    | admin    |

## Features

- **Role-based access control** — separate views and permissions for `USER` and `ADMIN` roles
- **Meal management** — create, update, delete, and filter meals by date/time range
- **Calorie tracking** — meals exceeding the daily calorie limit are highlighted in red
- **User management** — admins can create, edit, enable/disable, and delete users
- **REST API** — full CRUD API for meals and users, documented with Swagger UI
- **Responsive UI** — Bootstrap 4 + jQuery DataTables with AJAX interactions and notifications
- **Internationalization** — English and Russian locales
- **XSS & CSRF protection** — input sanitization via jsoup, CSRF tokens on all forms
- **Second-level cache** — Ehcache for user data to reduce database load
- **100+ integration tests** — service and REST controller tests with Spring MVC Test + AssertJ

## Tech Stack

| Layer          | Technology                                                      |
|----------------|-----------------------------------------------------------------|
| Language       | Java 16                                                         |
| Framework      | Spring MVC 5.3, Spring Security 5.5, Spring Data JPA 2.5       |
| ORM / DB       | Hibernate 5.5, PostgreSQL, HSQLDB                               |
| REST / JSON    | Jackson 2.12, Swagger 2 / Springfox                             |
| Cache          | Ehcache 3.9 (JCache API)                                        |
| Frontend       | Bootstrap 4.6, jQuery 3.6, DataTables, Noty, datetimepicker     |
| View           | JSP / JSTL                                                      |
| Build          | Maven 3, WAR packaging                                          |
| Server         | Apache Tomcat 9                                                 |
| Testing        | JUnit 5, AssertJ, Hamcrest, Spring MVC Test, JsonPath           |
| Logging        | SLF4J + Logback                                                 |
| Deployment     | Render (Docker + webapp-runner)                                 |

## Architecture

The application follows a classic layered architecture:

```
┌──────────────────────────────────┐
│   Web Layer (Spring MVC)         │
│   UI Controllers + REST API      │
├──────────────────────────────────┤
│   Service Layer                  │
│   Business logic, transactions   │
├──────────────────────────────────┤
│   Repository Layer               │
│   Spring Data JPA / JPA / JDBC   │
├──────────────────────────────────┤
│   Database                       │
│   PostgreSQL  /  HSQLDB          │
└──────────────────────────────────┘
```

Three interchangeable persistence implementations are provided and selected via Spring profiles:
- `datajpa` — Spring Data JPA (default)
- `jpa` — plain JPA / Hibernate
- `jdbc` — Spring JDBC

## Getting Started

### Prerequisites

- Java 16+
- Maven 3.6+
- PostgreSQL 13+ (or use the `hsqldb` profile for an in-memory database)

### Database setup (PostgreSQL)

```sql
CREATE DATABASE topjava;
CREATE USER topjava WITH PASSWORD 'topjava';
GRANT ALL PRIVILEGES ON DATABASE topjava TO topjava;
```

Initialize the schema and seed data:

```bash
psql -U topjava -d topjava -f src/main/resources/db/initDB_psql.sql
psql -U topjava -d topjava -f src/main/resources/db/populateDB.sql
```

### Run with Tomcat (Maven Cargo plugin)

```bash
mvn package cargo:run -P postgres,datajpa
```

The application will be available at `http://localhost:8080/topjava/`.

### Run with HSQLDB (no PostgreSQL required)

```bash
mvn package cargo:run -P hsqldb,datajpa
```

### Run tests

```bash
mvn test -P hsqldb,datajpa
```

## REST API

The REST API is documented via Swagger UI:

```
http://localhost:8080/topjava/swagger-ui.html
```

Key endpoints:

| Method | Endpoint                          | Description                   | Role  |
|--------|-----------------------------------|-------------------------------|-------|
| GET    | `/rest/meals`                     | Get all meals for current user | USER |
| POST   | `/rest/meals`                     | Create a meal                 | USER  |
| PUT    | `/rest/meals/{id}`                | Update a meal                 | USER  |
| DELETE | `/rest/meals/{id}`                | Delete a meal                 | USER  |
| GET    | `/rest/admin/users`               | Get all users                 | ADMIN |
| POST   | `/rest/admin/users`               | Create a user                 | ADMIN |
| GET    | `/rest/profile`                   | Get current user profile      | USER  |
| POST   | `/rest/profile/register`          | Register a new user           | ANY   |

## Deploying to Render

The project includes a `Dockerfile` for cloud deployment. Steps to deploy on [Render](https://render.com):

**1. Create a PostgreSQL database**
- Dashboard → New → PostgreSQL
- Copy the **Internal Database URL** — it looks like `postgresql://user:pass@host/db`

**2. Create a Web Service**
- New → Web Service → connect your GitHub repo
- Runtime: **Docker**
- Add environment variable:
  ```
  DATABASE_URL = <Internal Database URL from step 1>
  ```

**3. Initialize the database schema** (once, after first deploy)
- Open the PostgreSQL service in Render → Shell
- Run:
  ```bash
  psql $DATABASE_URL -f /dev/stdin < initDB.sql
  psql $DATABASE_URL -f /dev/stdin < populateDB.sql
  ```
- Or connect externally using the **External Database URL** and run the scripts from `src/main/resources/db/`

> Note: free tier instances on Render spin down after inactivity — first request may take ~30 seconds.

## Project Structure

```
src/
├── main/
│   ├── java/ru/javawebinar/topjava/
│   │   ├── model/          # JPA entities (User, Meal, Role)
│   │   ├── repository/     # Data access (datajpa / jpa / jdbc)
│   │   ├── service/        # Business logic (MealService, UserService)
│   │   ├── to/             # Data Transfer Objects (MealTo, UserTo)
│   │   ├── web/            # Spring MVC controllers (UI + REST)
│   │   └── util/           # Helpers, validators, exception types
│   ├── resources/
│   │   ├── db/             # SQL init scripts
│   │   └── spring/         # Spring XML configs per profile
│   └── webapp/             # JSP views, static assets
└── test/                   # 100+ JUnit 5 integration tests
```
