/**
 * Front-end logic (Vanilla JS)
 * - Talks to the Spring Boot REST API using fetch()
 * - Renders tasks in the DOM
 *
 * Dynamic aspects required by the assignment:
 * 1) Load tasks from backend (GET /api/tasks)
 * 2) Create + Update + Delete tasks via backend (POST/PUT/DELETE)
 */

const API_BASE = "/api/tasks";

const taskForm = document.getElementById("taskForm");
const titleInput = document.getElementById("title");
const dueDateInput = document.getElementById("dueDate");
const priorityInput = document.getElementById("priority");
const formError = document.getElementById("formError");

const taskList = document.getElementById("taskList");
const emptyState = document.getElementById("emptyState");

const filterButtons = Array.from(document.querySelectorAll(".filter"));
let currentFilter = "all";
let tasksCache = [];

function todayIso() {
  const d = new Date();
  const yyyy = d.getFullYear();
  const mm = String(d.getMonth() + 1).padStart(2, "0");
  const dd = String(d.getDate()).padStart(2, "0");
  return `${yyyy}-${mm}-${dd}`;
}

dueDateInput.value = todayIso();

function setError(msg) {
  formError.textContent = msg || "";
}

async function apiGetTasks() {
  const res = await fetch(API_BASE);
  if (!res.ok) throw new Error("Failed to load tasks");
  return await res.json();
}

async function apiCreateTask(payload) {
  const res = await fetch(API_BASE, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  if (!res.ok) {
    const body = await res.json().catch(() => null);
    const msg = body?.fields ? JSON.stringify(body.fields) : "Failed to create task";
    throw new Error(msg);
  }
  return await res.json();
}

async function apiUpdateTask(id, payload) {
  const res = await fetch(`${API_BASE}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error("Failed to update task");
  return await res.json();
}

async function apiDeleteTask(id) {
  const res = await fetch(`${API_BASE}/${id}`, { method: "DELETE" });
  if (!res.ok && res.status !== 404) throw new Error("Failed to delete task");
}

function applyFilter(allTasks) {
  if (currentFilter === "open") return allTasks.filter(t => !t.done);
  if (currentFilter === "done") return allTasks.filter(t => t.done);
  return allTasks;
}

function render() {
  taskList.innerHTML = "";

  const visible = applyFilter(tasksCache);

  emptyState.style.display = visible.length === 0 ? "block" : "none";

  for (const task of visible) {
    const li = document.createElement("li");
    li.className = "task" + (task.done ? " done" : "");

    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.checked = !!task.done;
    checkbox.title = "Mark done/undone";
    checkbox.addEventListener("change", async () => {
      // Toggle done
      try {
        await apiUpdateTask(task.id, {
          title: task.title,
          dueDate: task.dueDate,
          priority: task.priority,
          done: checkbox.checked,
        });
        await refresh();
      } catch (e) {
        alert(e.message || "Update failed");
        checkbox.checked = !checkbox.checked; // rollback
      }
    });

    const meta = document.createElement("div");
    meta.className = "meta";

    const title = document.createElement("div");
    title.className = "title";
    title.textContent = task.title;

    const badges = document.createElement("div");
    badges.className = "badges";

    const due = document.createElement("span");
    due.className = "badge";
    due.textContent = `Due: ${task.dueDate}`;

    const prio = document.createElement("span");
    prio.className = "badge";
    prio.textContent = `Priority: ${task.priority}`;

    badges.appendChild(due);
    badges.appendChild(prio);

    meta.appendChild(title);
    meta.appendChild(badges);

    const actions = document.createElement("div");
    actions.className = "actions";

    const delBtn = document.createElement("button");
    delBtn.className = "btn-danger";
    delBtn.type = "button";
    delBtn.textContent = "Delete";
    delBtn.addEventListener("click", async () => {
      if (!confirm("Delete this task?")) return;
      try {
        await apiDeleteTask(task.id);
        await refresh();
      } catch (e) {
        alert(e.message || "Delete failed");
      }
    });

    actions.appendChild(delBtn);

    li.appendChild(checkbox);
    li.appendChild(meta);
    li.appendChild(actions);

    taskList.appendChild(li);
  }
}

async function refresh() {
  tasksCache = await apiGetTasks();
  render();
}

taskForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  setError("");

  const title = titleInput.value.trim();
  const dueDate = dueDateInput.value;
  const priority = priorityInput.value;

  if (!title) {
    setError("Please enter a title.");
    return;
  }
  if (!dueDate) {
    setError("Please choose a due date.");
    return;
  }

  try {
    await apiCreateTask({ title, dueDate, priority });
    titleInput.value = "";
    dueDateInput.value = todayIso();
    priorityInput.value = "Medium";
    await refresh();
  } catch (err) {
    setError(err.message || "Create failed");
  }
});

filterButtons.forEach(btn => {
  btn.addEventListener("click", async () => {
    filterButtons.forEach(b => b.classList.remove("active"));
    btn.classList.add("active");
    currentFilter = btn.dataset.filter;
    render();
  });
});

// Initial load (dynamic aspect #1)
refresh().catch(() => {
  emptyState.textContent = "Could not load tasks. Make sure the backend is running.";
});
