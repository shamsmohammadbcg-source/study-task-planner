package com.studyplanner.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request payload for creating a new task.
 */
public class CreateTaskRequest {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "dueDate must be in YYYY-MM-DD format")
    private String dueDate;

    @Pattern(regexp = "^(Low|Medium|High)$", message = "priority must be Low, Medium, or High")
    private String priority;

    public CreateTaskRequest() {}

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
}
