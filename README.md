# Academa - Online Learning Management Systems

## Overview
Academa is a Spring Boot-based application for managing courses, students, and instructors in an educational setting. It uses PostgreSQL and Docker Compose for deployment.

## Features
- Manage courses, students, and instructors.
- Enroll students in multiple courses with validation (e.g., time conflict checks).
- Complex queries to find enrolled students and assigned instructors.

## Tech Stack
- **Backend**: Spring Boot
- **Database**: PostgreSQL
- **Containerization**: Docker Compose

## Installation
1. Clone the repository.
2. Copy the `.env.template` file to `.env` and update the environment variables.
3. Run `docker-compose up --build -d ` to start the application.
4. Access at `http://localhost:8080`.
