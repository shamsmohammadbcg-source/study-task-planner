# 📚 Study Task Planner (DLBCSPJWD01)

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.9+-blue)
![Build](https://img.shields.io/badge/Build-Passing-success)

A small web application to **create, prioritize, and complete study tasks**.
It was built as a portfolio project for the IU course **Project Java and Web Development (DLBCSPJWD01)**.

## Features (MVP)
- View all tasks (loaded from the backend on page load)
- Add a task with:
  - title
  - due date (`YYYY-MM-DD`)
  - priority (`Low | Medium | High`)
- Mark a task as done / undo done
- Delete a task
- Filter tasks in the UI: **All / Open / Done**
- Responsive UI (desktop + mobile viewport)

## Tech stack
- **Backend:** Java 17 + Spring Boot (REST API)
- **Frontend:** HTML + CSS + Vanilla JavaScript (Fetch API)
- **Data exchange format:** JSON
- **Persistence:** file-based JSON storage on the server (`data/tasks.json`)
- **Tests:** Spring Boot integration tests (MockMvc)

## Project structure (important folders)
```
study-task-planner/
├─ src/main/java/com/studyplanner/
│  ├─ model/        # Domain model (Task)
│  ├─ web/          # REST controller + request DTOs + exception handler
│  ├─ service/      # Business logic
│  └─ repo/         # JSON file persistence (data/tasks.json)
├─ src/main/resources/
│  ├─ static/       # Frontend: index.html, styles.css, app.js
│  └─ application.properties
├─ src/test/java/   # Integration tests (MockMvc)
└─ data/tasks.json  # Storage file (created automatically if missing)
```

## Requirements
- **Java 17 (JDK 17)**
- **Maven 3.9+**

Check:
```bash
java -version
mvn -v
```

## Run the application (local)
From the project root:
```bash
mvn spring-boot:run
```

Open in your browser:
```text
http://localhost:8080
```

> The frontend is served by Spring Boot from `src/main/resources/static`.

### Change the port (optional)
Default is `8080`. You can change it in:
`src/main/resources/application.properties` (key: `server.port`).

## Run tests
```bash
mvn test
```

What the tests do:
- Reset `data/tasks.json` before each test
- Verify the REST endpoints work:
  - create (POST)
  - list (GET)
  - update (PUT)
  - delete (DELETE)

## Build a runnable JAR (optional)
```bash
mvn clean package
```

Run the generated JAR:
```bash
java -jar target/study-task-planner-0.0.1-SNAPSHOT.jar
```

Then open:
```text
http://localhost:8080
```

## REST API documentation

### Data model: Task
A task contains:
- `id` (number)
- `title` (string, required)
- `dueDate` (string, format `YYYY-MM-DD`)
- `priority` (`Low` | `Medium` | `High`)
- `done` (boolean)

### Endpoints
- `GET /api/tasks`  
  Returns a JSON array of tasks.

- `POST /api/tasks`  
  Creates a new task. Returns the created task (HTTP 201).

- `PUT /api/tasks/{id}`  
  Updates an existing task (including toggling `done`). Returns the updated task (HTTP 200).

- `DELETE /api/tasks/{id}`  
  Deletes a task. Returns HTTP 204 if deleted.

### Example requests (curl)

Create a task:
```bash
curl -X POST http://localhost:8080/api/tasks   -H "Content-Type: application/json"   -d '{"title":"Write Phase 2 slides","dueDate":"2026-02-20","priority":"High"}'
```

List tasks:
```bash
curl http://localhost:8080/api/tasks
```

Update a task (toggle done):
```bash
curl -X PUT http://localhost:8080/api/tasks/1   -H "Content-Type: application/json"   -d '{"title":"Write Phase 2 slides","dueDate":"2026-02-20","priority":"High","done":true}'
```

Delete a task:
```bash
curl -X DELETE http://localhost:8080/api/tasks/1
```

### Validation rules (backend)
- `title` must not be blank
- `dueDate` must match `YYYY-MM-DD`
- `priority` must be `Low`, `Medium`, or `High`

If validation fails, the backend returns HTTP 400 with a JSON body like:
```json
{
  "error": "Validation failed",
  "fields": {
    "title": "Title must not be blank",
    "priority": "priority must be Low, Medium, or High"
  }
}
```

## Server-side persistence
Tasks are stored in:
- `data/tasks.json`

Notes:
- The file is created automatically if it does not exist.
- The backend writes via a temporary file and then replaces the original file (safer write).
- To reset all tasks, stop the server and delete `data/tasks.json` (it will be recreated).

## Responsive UI
The UI layout adapts to smaller screens via CSS (mobile-friendly spacing and stacking).
You can preview mobile view in Chrome DevTools (device toolbar).

## Troubleshooting
**Port 8080 already in use**
- Stop the other program using port 8080, or change `server.port` in `application.properties`.

**`data/tasks.json` cannot be written**
- Make sure the app has write permissions in the project directory.
- Try deleting the `data/` folder and restart (it will be recreated).

**App loads but buttons don’t work**
- Check browser DevTools Console for errors.
- Ensure the backend is running and `http://localhost:8080/api/tasks` returns data.

---
If you’re using this repository for the IU portfolio:
- The Phase 2 PPTX should contain screenshots (desktop + responsive) and an embedded screencast.
- The Phase 3 submission requires exporting and zipping this repository folder.

