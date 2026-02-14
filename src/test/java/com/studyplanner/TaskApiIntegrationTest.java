package com.studyplanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplanner.web.CreateTaskRequest;
import com.studyplanner.web.UpdateTaskRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Minimal integration tests for the Task API.
 *
 * Purpose:
 * - Prove endpoints work
 * - Supports portfolio evaluation (testing & correctness)
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskApiIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @BeforeEach
    void resetStorage() throws Exception {
        // Ensure clean slate for each test run
        Path p = Paths.get("data", "tasks.json");
        Files.createDirectories(p.getParent());
        Files.writeString(p, "[]", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Test
    void create_then_list_tasks() throws Exception {
        CreateTaskRequest req = new CreateTaskRequest();
        req.setTitle("Test task");
        req.setDueDate("2030-01-01");
        req.setPriority("Medium");

        mvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.title", is("Test task")))
                .andExpect(jsonPath("$.done", is(false)));

        mvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(empty()))));
    }

    @Test
    void update_task_done_flag() throws Exception {
        // Create first
        CreateTaskRequest create = new CreateTaskRequest();
        create.setTitle("Toggle done");
        create.setDueDate("2030-02-02");
        create.setPriority("Low");

        String createdJson = mvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long id = mapper.readTree(createdJson).get("id").asLong();

        UpdateTaskRequest update = new UpdateTaskRequest();
        update.setTitle("Toggle done");
        update.setDueDate("2030-02-02");
        update.setPriority("Low");
        update.setDone(true);

        mvc.perform(put("/api/tasks/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done", is(true)));
    }

    @Test
    void delete_task() throws Exception {
        CreateTaskRequest create = new CreateTaskRequest();
        create.setTitle("Delete me");
        create.setDueDate("2030-03-03");
        create.setPriority("High");

        String createdJson = mvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long id = mapper.readTree(createdJson).get("id").asLong();

        mvc.perform(delete("/api/tasks/" + id))
                .andExpect(status().isNoContent());
    }
}
