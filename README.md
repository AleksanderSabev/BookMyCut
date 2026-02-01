# ✂️ BookMyCut

**BookMyCut** is a Spring Boot REST API for managing barber/salon bookings.  
It provides authentication, role-based authorization, appointment scheduling, employee management, and procedure catalog management.

The backend is designed to be consumed by a **Single Page Application (SPA)** frontend.

---

## 🚀 Features

- User registration & login
- Stateless JWT authentication
- Role-based authorization (ADMIN / USER)
- Appointment booking and management
- Employee management (admin only)
- Procedure catalog management (admin only)
- Clean REST API design

---

## 🛠 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- Jakarta Validation

---

## 🔐 Security

- Stateless JWT-based authentication
- Authorization via `@PreAuthorize`
- Roles are enforced only on the backend
- Frontend stores JWT and sends it in the `Authorization` header
- No server-side sessions

---

## 📂 Architecture

- REST API
- Controller → Service → Repository
- DTO-based communication
- Centralized exception handling
- Clear separation of concerns

---

## 📄 DTO Usage

- DTOs are used for all API input/output
- Entities are never exposed directly
- Authentication responses include:
  - username
  - JWT token
  - role
  - token expiration time

---

## ⚙️ Configuration

### JWT
- Token expiration: 30 minutes

### Database
- PostgreSQL
- JPA / Hibernate for persistence

---

## 📌 Project Status

- Backend implemented
- JWT & security complete
- Role-based authorization enforced
- SPA frontend in progress

---

## 👨‍💻 Author

**Aleksander Sabev**  
Java / Spring Boot Developer
