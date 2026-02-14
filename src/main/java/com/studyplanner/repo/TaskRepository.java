package com.studyplanner.repo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplanner.model.Task;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based repository that persists tasks to a JSON file (MVP-friendly).
 *
 * Why this approach?
 * - Simple to understand (no DB setup needed).
 * - Meets the requirement of server-side storage.
 */
@Repository
public class TaskRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Path storagePath = Paths.get("data", "tasks.json");

    public TaskRepository() {
        ensureStorageExists();
    }

    private void ensureStorageExists() {
        try {
            Files.createDirectories(storagePath.getParent());
            if (!Files.exists(storagePath)) {
                Files.writeString(storagePath, "[]", StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize storage at " + storagePath.toAbsolutePath(), e);
        }
    }

    public synchronized List<Task> findAll() {
        try {
            String json = Files.readString(storagePath);
            if (json == null || json.isBlank()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(json, new TypeReference<List<Task>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read tasks from " + storagePath.toAbsolutePath(), e);
        }
    }

    public synchronized void saveAll(List<Task> tasks) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tasks);

            // Write to a temp file first (safer), then replace.
            Path tmp = storagePath.resolveSibling("tasks.tmp.json");
            Files.writeString(tmp, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(tmp, storagePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);

        } catch (IOException e) {
            throw new RuntimeException("Failed to write tasks to " + storagePath.toAbsolutePath(), e);
        }
    }
}
