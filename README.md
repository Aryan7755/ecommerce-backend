# Ecommerce Backend

A production-oriented Spring Boot REST API for an e-commerce platform with JWT authentication, role-based authorization, product catalog, cart management, address management, and order checkout with price snapshotting.

## Architecture

Client
↓
Spring Security / JWT Filter
↓
Controller
↓
Service
↓
Repository
↓
MySQL

## Features

- JWT-based authentication with access and refresh tokens
- Role-based authorization (`USER`, `ADMIN`)
- Product and category management
- Product search, filtering, and pagination
- Shopping cart management
- User address management
- Order checkout and order history
- Admin order status management
- Price snapshotting in order items
- Global exception handling
- Request/response logging
- Bean validation
- Flyway database migrations
- Swagger/OpenAPI documentation
- Unit testing with JUnit 5 and Mockito
- Integration testing with Testcontainers
- Dockerized application with Docker Compose

## Tech Stack

- Java 17
- Spring Boot 4.1
- Spring Security
- Spring Data JPA / Hibernate
- MySQL 8
- Flyway
- JJWT
- JUnit 5
- Mockito
- Testcontainers
- Maven
- Docker / Docker Compose
- Swagger / OpenAPI
- Lombok

## Project Structure

src/main/java/com/aryan/ecommerce_backend
├── auth
├── user
├── product
├── category
├── cart
├── order
├── address
├── security
├── exception
└── config

## Running Locally

### Prerequisites

- Java 17+
- Maven
- MySQL 8+
- Docker Desktop

Configure the required database and JWT environment variables, then run:

mvn spring-boot:run

The application runs on:

http://localhost:8080

## Running with Docker

### 1. Configure Environment Variables

Create a `.env` file in the project root based on `.env.example`:

DB_PASSWORD=your_database_password
DB_ROOT_PASSWORD=your_root_password
JWT_SECRET=your_jwt_secret

Do not commit `.env` to Git.

### 2. Start the Application

docker compose up --build

The Docker Compose setup starts:

- Spring Boot application
- MySQL database
- Persistent MySQL volume

The application is available at:

http://localhost:8081

The application listens on port `8080` inside the container and is mapped to port `8081` on the host.

### 3. Stop the Application

docker compose down

## API Documentation

Swagger UI:

http://localhost:8081/swagger-ui.html

OpenAPI specification:

http://localhost:8081/v3/api-docs

## Testing

Run the complete test suite:

mvn test

Integration tests use Testcontainers with MySQL, so Docker must be running.

## Database Migrations

Database schema changes are managed using Flyway.

Migration files are located under:

src/main/resources/db/migration

Flyway automatically applies pending migrations when the application starts.

## Security

The API uses:

- JWT authentication
- Stateless session management
- BCrypt password hashing
- Role-based access control
- Custom `401 Unauthorized` and `403 Forbidden` handlers
- Protected endpoints through Spring Security

## Git

Sensitive configuration such as `.env` must not be committed.

.env

## License

This project is licensed under the MIT License.