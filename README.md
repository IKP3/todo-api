# todo-api — Spring Boot To‑Do REST API

A minimal REST API for managing users and their tasks. Built with Java 21, Spring Boot 3, Spring Data JPA, PostgreSQL (dev/prod), and H2 (tests). Includes validation, pagination, and OpenAPI docs.

## Features
- Users CRUD and per-user Tasks CRUD
- Pagination for listing users and tasks
- Request validation via `jakarta.validation`
- OpenAPI/Swagger UI documentation
- Integration tests with in-memory H2

## Tech Stack
- Java 21, Spring Boot 3 (Web, Data JPA, Validation)
- PostgreSQL (runtime), H2 (tests)
- Lombok, ModelMapper, Springdoc OpenAPI
- Maven, Docker Compose (Postgres)

## Quickstart (Local)
Prereqs: Java 21, Maven, Docker

1) Start PostgreSQL
- From `todo-api/`:
  - `docker compose up -d db`

2) Run the API
- From `todo-api/`:
  - `./mvnw spring-boot:run`

3) Open API docs
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Configuration
Defaults (see `todo-api/src/main/resources/application.properties`):
- `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/tododb`
- `SPRING_DATASOURCE_USERNAME=dbuser`
- `SPRING_DATASOURCE_PASSWORD=dbpassword`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=update`

Override with environment variables or a profile as needed.

## Example Requests
- Create user
  - `curl -s -X POST http://localhost:8080/api/v1/users -H 'Content-Type: application/json' -d '{"name":"Igor"}'`
- Get user
  - `curl -s http://localhost:8080/api/v1/users/1`
- Update user
  - `curl -s -X PUT http://localhost:8080/api/v1/users/1 -H 'Content-Type: application/json' -d '{"name":"Updated"}'`
- Delete user
  - `curl -s -X DELETE http://localhost:8080/api/v1/users/1 -i`

- Create task (for user 1)
  - `curl -s -X POST http://localhost:8080/api/v1/users/1/tasks -H 'Content-Type: application/json' -d '{"description":"Buy milk"}'`
- Get a task
  - `curl -s http://localhost:8080/api/v1/users/1/tasks/1`
- List tasks (paged)
  - `curl -s 'http://localhost:8080/api/v1/users/1/tasks?page=0&size=10'`

## Testing
- Run tests: `./mvnw -q test`
- Tests use H2 via `todo-api/src/test/resources/application.properties` (PostgreSQL compatibility mode).

## Project Structure
- `todo-api/src/main/java/.../controllers` — REST controllers
- `todo-api/src/main/java/.../services` — business logic
- `todo-api/src/main/java/.../repositories` — Spring Data repositories
- `todo-api/src/main/java/.../domain` — entities and DTOs
- `todo-api/src/main/java/.../mappers` — ModelMapper adapters
- `todo-api/src/test/java/...` — integration tests (MockMvc)

## Notes
- A `docker-compose.yml` (under `todo-api/`) is provided for Postgres.
- Swagger UI is enabled via Springdoc for quick exploration.
