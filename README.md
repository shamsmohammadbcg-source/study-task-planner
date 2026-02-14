# Study Task Planner (DLBCSPJWD01)

A small web application to create, prioritize, and complete study tasks.

## Tech stack
- Backend: Java + Spring Boot (REST API)
- Frontend: HTML + CSS + Vanilla JavaScript (Fetch API)
- Storage: JSON file on server (`data/tasks.json`)

## How to run (local)

### Requirements
- Java 17 (JDK)
- Maven 3.9+

### Run the application
From the project root:

```bash
mvn spring-boot:run
```

Then open:
- http://localhost:8080

The UI is served from Spring Boot (`src/main/resources/static`).

## REST API
- `GET /api/tasks` – list tasks
- `POST /api/tasks` – create task
- `PUT /api/tasks/{id}` – update task (including done)
- `DELETE /api/tasks/{id}` – delete task

## Data model
A task contains:
- `id` (number)
- `title` (string)
- `dueDate` (string, `YYYY-MM-DD`)
- `priority` (Low | Medium | High)
- `done` (boolean)

## Notes
- Data is stored in `data/tasks.json` in the working directory.
- For an MVP, this avoids database setup while still providing server-side persistence.
