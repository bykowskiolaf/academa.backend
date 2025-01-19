# Academa - Online Learning Management Systems

### Overview
Academa is a Spring Boot-based application for managing courses, students, and instructors in an educational setting. It uses PostgreSQL and Docker Compose for deployment.

### Tech Stack
- **Backend**: Spring Boot
- **Database**: PostgreSQL
- **Containerization**: Docker Compose

## Project Specification

### 1. **Data Model Description**

This project uses a **relational data model** to represent the entities and their relationships within an online learning platform.
#### Entities:

- **Course**:
    - Fields: `id`, `name`, `description`, `schedule`
    - Relationships:
        - A course is taught by one instructor (Many-to-One relationship).
        - Students enroll in courses, creating a Many-to-Many relationship between `Course` and `Student`.

- **Instructor**:
    - Fields: `id`, `name`, `expertise`
    - Relationships:
        - An instructor can teach multiple courses, creating a One-to-Many relationship with `Course`.

- **Student**:
    - Fields: `id`, `name`, `email`
    - Relationships:
        - A student can enroll in multiple courses, creating a Many-to-Many relationship with `Course`.

- **Enrollment**:
    - Represents the enrollment of students in courses.
    - Fields: `student_id`, `course_id`, `enrollment_date`

### 2. **Complex Query Description**

The complex query will retrieve all students enrolled in a specific course, along with the instructors teaching that course. This query will use the relationship between `Student`, `Course`, and `Instructor` to gather the relevant data.

### 3. **Business Logic Operation**

The business logic ensures that students can enroll in multiple courses, but with the following validation checks:
- The student cannot enroll in courses that overlap in their scheduled times.
- The enrollment will only proceed if the course still has available seats.

This validation ensures that students' schedules are conflict-free and that the course has the capacity to accommodate them.


## Installation
1. Clone the repository.
2. Copy the `.env.template` file to `.env` and update the environment variables.
3. Run `docker-compose up --build -d ` to start the application.
4. Access at `http://localhost:8080`.
