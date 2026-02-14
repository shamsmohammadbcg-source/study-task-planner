package com.studyplanner.web;

import com.studyplanner.model.Task;
import com.studyplanner.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API controller.
 *
 * Endpoints:
 * - GET    /api/tasks
 * - POST   /api/tasks
 * - PUT    /api/tasks/{id}
 * - DELETE /api/tasks/{id}
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody CreateTaskRequest req) {
        Task created = service.create(req.getTitle(), req.getDueDate(), req.getPriority());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable long id, @Valid @RequestBody UpdateTaskRequest req) {
        Task updated = new Task(id, req.getTitle(), req.getDueDate(), req.getPriority(), req.isDone());
        return service.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        boolean removed = service.delete(id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
