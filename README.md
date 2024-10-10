# Nottify

## Overview
**Nottify** is a Spring Boot application designed for users to create and manage posts as well as handle user registration, authentication, and updates. The project includes JWT-based security for managing access and interactions within the system.

## Features
- User registration and authentication
- CRUD operations for posts
- JWT-based authentication
- Integration with PostgreSQL
- API documentation with Swagger

## Requirements
To run the project, make sure you have the following installed:
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- or
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

## Setup Instructions

1. **Clone the repository**:
    ```bash
    git clone https://github.com/sttmeir/nottify.git
    ```

2. **Navigate to the project folder**:
    ```bash
    cd nottify
    ```

3. **Build and run the application using Docker**:
    ```bash
    docker-compose up --build
    ```

## API Documentation
The API is documented using Swagger. You can access the documentation at:

http://localhost:8080/swagger-ui.html


## Configuration
The project uses the following environment variables (configured in `docker-compose.yml`):

- `SPRING_DATASOURCE_URL`: jdbc:postgresql://db:5432/nottify
- `SPRING_DATASOURCE_USERNAME`: Database postgres
- `SPRING_DATASOURCE_PASSWORD`: Database ideappad147

## Libraries and Technologies Used 
- Spring Boot — main framework for building the REST API
- Spring Security — for user authentication and authorization
- Hibernate — ORM for database interaction
- Lombok — to reduce boilerplate code
- PostgreSQL — as the database for storing user and post data
- Swagger - for documenting the API
