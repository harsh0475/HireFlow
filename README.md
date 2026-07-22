# 🚀 HireFlow - Recruitment Management System

HireFlow is a full-stack Recruitment Management System designed to streamline the hiring process for recruiters, candidates, and administrators. It provides secure authentication, job management, candidate applications, interview scheduling, dashboards, email notifications, file uploads, and role-based access control.

---

# 📌 Features

## 🔐 Authentication & Security

- JWT Authentication
- Refresh Token Authentication
- Secure Logout
- Forgot Password
- Reset Password
- Change Password
- BCrypt Password Encryption
- Spring Security
- Role-Based Authorization
- Access Token & Refresh Token

---

## 👥 User Roles

- Admin
- Recruiter
- Candidate

---

## 🏢 Company Management

- Create Company
- Update Company
- Delete Company
- View Company Details

---

## 💼 Job Management

- Create Job
- Update Job
- Delete Job
- Search Jobs
- Filter Jobs
- Pagination
- Company-wise Jobs
- Recruiter-wise Jobs

---

## 👨‍💻 Candidate Module

- Candidate Profile
- Resume Upload
- Skills Management
- Education
- Experience
- Portfolio
- LinkedIn Profile

---

## 👔 Recruiter Module

- Recruiter Profile
- Company Association
- Manage Posted Jobs

---

## 📄 Job Applications

- Apply for Jobs
- Track Application Status
- Withdraw Application
- Recruiter Application Management

---

## 📅 Interview Module

- Schedule Interview
- Update Interview
- Interview Status
- Interview Feedback
- Interview Outcome
- Meeting Link

---

## 📊 Dashboard

### Candidate Dashboard

- Applied Jobs
- Interview Count
- Application Status

### Recruiter Dashboard

- Posted Jobs
- Applications
- Interviews
- Hiring Statistics

### Admin Dashboard

- Platform Statistics
- User Analytics

---

## 📧 Email Module

- Welcome Email
- Password Reset Email
- Interview Scheduled Email
- Interview Updated Email
- Interview Cancelled Email
- Application Status Email

---

## 📁 File Upload Module

- Resume Upload
- Company Logo Upload
- Profile Picture Upload
- File Validation
- Secure Storage

---

## 🔍 Search & Filtering

- Dynamic Filtering
- Spring Specifications
- Pagination
- Sorting

---

## 🛡 Validation

- Bean Validation
- Custom Validation
- Global Exception Handling
- Standard API Responses

---

# 🛠 Tech Stack

## Backend

- Java 21
- Spring Boot 3.5.x
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- JWT
- MapStruct
- Lombok
- Spring Validation
- Spring Mail

## Database

- PostgreSQL

## Documentation

- Swagger / OpenAPI

---

# 🏗 Architecture

```
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
PostgreSQL Database
```

Project follows a layered architecture with proper separation of concerns.

---

# 📂 Project Structure

```
backend/
│
├── config/
├── controller/
├── dto/
│   ├── request/
│   └── response/
├── entity/
├── entity/enums/
├── exception/
├── mapper/
├── repository/
├── security/
├── service/
│   ├── impl/
├── specification/
├── util/
└── HireflowApplication.java
```

---

# 🔑 Authentication Flow

```
Register
      │
      ▼
Login
      │
      ▼
Access Token + Refresh Token
      │
      ▼
Authenticated APIs
      │
      ▼
Refresh Token
      │
      ▼
New Access Token
```

---

# 📌 API Features

- RESTful APIs
- DTO Mapping using MapStruct
- Standardized ApiResponse
- Global Exception Handling
- Pagination
- Filtering
- Validation
- Role-Based Authorization

---

# 🗄 Database

Main entities include:

- User
- Company
- Job
- CandidateProfile
- RecruiterProfile
- Application
- Interview
- RefreshToken

---

# 🚀 Getting Started

## Clone Repository

```bash
git clone https://github.com/harsh0475/HireFlow.git
```

---

## Navigate

```bash
cd HireFlow/backend
```

---

## Configure Database

Update:

```
application.yml
```

Example:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/hireflow
    username: postgres
    password: your_password
```

---

## Run

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

---

# 📖 API Documentation

Swagger UI

```
http://localhost:8081/swagger-ui/index.html
```

---

# 🔒 Roles

| Role | Permissions |
|------|-------------|
| ADMIN | Manage Platform |
| RECRUITER | Manage Companies, Jobs, Interviews |
| CANDIDATE | Apply Jobs, Manage Profile |

---

# 📈 Future Enhancements

- Notification Module
- Admin Analytics
- Docker Support
- CI/CD Pipeline
- Unit Testing
- Integration Testing
- Performance Optimization
- Caching
- Monitoring

---

# 👨‍💻 Author

**Harshit Kumar Singh**

GitHub:
https://github.com/harsh0475

---

# 📄 License

This project is developed for educational and portfolio purposes.