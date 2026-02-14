package com.studyplanner.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Domain model representing a study task.
 *
 * NOTE: To keep the project simple, dueDate is stored as a string in YYYY-MM-DD format.
 */
public class Task {

    private long id;

    @NotBlank(message = "Title must not be blank")
    private String title;

    /**
     * Expected format: YYYY-MM-DD (e.g., 2026-01-22).
     * This is stored as a string to avoid extra date configuration.
     */
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "dueDate must be in YYYY-MM-DD format")
    private String dueDate;

    /**
     * Priority values (MVP): Low, Medium, High.
     */
    @Pattern(regexp = "^(Low|Medium|High)$", message = "priority must be Low, Medium, or High")
    private String priority;

    private boolean done;

    public Task() {}

    public Task(long id, String title, String dueDate, String priority, boolean done) {
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.done = done;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
