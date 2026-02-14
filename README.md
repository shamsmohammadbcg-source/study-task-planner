# 📚 Study Task Planner (DLBCSPJWD01)

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.9+-blue)
![Build](https://img.shields.io/badge/Build-Passing-success)

A full-stack Study Task Management web application built with **Spring Boot**.  
It allows users to create, prioritize, update, complete, and delete study tasks through a RESTful API and a simple frontend interface.

---

## 🚀 Features

- Create new study tasks
- View all tasks
- Update task details and completion status
- Delete tasks
- Input validation (date format and priority)
- File-based persistence using JSON
- RESTful API with proper HTTP status codes

---

## 🛠 Tech Stack

**Backend**
- Java 17
- Spring Boot 3
- Spring Web (REST API)
- Jakarta Validation

**Frontend**
- HTML
- CSS
- Vanilla JavaScript (Fetch API)

**Build Tool**
- Maven 3.9+

**Storage**
- JSON file (`data/tasks.json`)

---

## 📋 Prerequisites

Make sure you have the following installed:

- Java 17 (JDK)
- Apache Maven 3.9+
- Git (to clone the repository)

Verify installations:

```bash
java -version
mvn -version