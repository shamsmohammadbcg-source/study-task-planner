package com.studyplanner.service;

import com.studyplanner.model.Task;
import com.studyplanner.repo.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for managing tasks.
 *
 * Responsibilities:
 * - Assign IDs on creation
 * - Update/delete tasks
 * - Persist changes via repository
 */
@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAll() {
        // Sort by dueDate then priority, just to keep the UI stable and nicer.
        List<Task> tasks = repository.findAll();
        tasks.sort(Comparator
                .comparing(Task::getDueDate, Comparator.nullsLast(String::compareTo))
                .thenComparing(Task::getPriority, Comparator.nullsLast(String::compareTo))
                .thenComparingLong(Task::getId)
        );
        return tasks;
    }

    public Task create(String title, String dueDate, String priority) {
        List<Task> tasks = repository.findAll();
        long nextId = tasks.stream().map(Task::getId).max(Long::compareTo).orElse(0L) + 1;

        Task task = new Task(nextId, title, dueDate, priority, false);
        tasks.add(task);
        repository.saveAll(tasks);
        return task;
    }

    public Optional<Task> update(long id, Task updated) {
        List<Task> tasks = repository.findAll();

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == id) {
                updated.setId(id);
                tasks.set(i, updated);
                repository.saveAll(tasks);
                return Optional.of(updated);
            }
        }
        return Optional.empty();
    }

    public boolean delete(long id) {
        List<Task> tasks = repository.findAll();
        boolean removed = tasks.removeIf(t -> t.getId() == id);
        if (removed) {
            repository.saveAll(tasks);
        }
        return removed;
    }
}
