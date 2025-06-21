# Sporty Challenge API

A Spring Boot ticketing application with JWT-based authentication using JWE (JSON Web Encryption).

## Getting Started

These instructions will help you run the application locally using Docker.

### Prerequisites

- Java 17 or higher
- Maven
- Docker (already installed)

### Running the Application

1. Build and run application:
   ```bash
   docker-compose up

2. The application will be available at http://localhost:8080

## Authentication

### Login Credentials

The application comes with two pre-configured users:

1. Regular User:
    - Username: `user`
    - Password: `user`
    - Role: USER

2. Agent:
    - Username: `agent`
    - Password: `agent`
    - Role: AGENT

### Authentication Flow

1. Get a token by sending a POST request to `/auth/login`:
   **Unix/Linux/macOS:**
   ```bash
   curl -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{"username":"user","password":"user"}'
   ```
   **Windows Command Prompt:**
   ```bash
   curl -X POST http://localhost:8080/auth/login ^ -H "Content-Type: application/json" ^ -d "{\"username\":\"user\",\"password\":\"user\"}"
   ```
2. Include the token in subsequent requests:

   **Unix/Linux/macOS:**
   ```bash
   curl -X GET http://localhost:8080/tickets -H "Authorization: Bearer YOUR_TOKEN_HERE"
   ```
   **Windows Command Prompt:**
   ```bash
   curl -X GET http://localhost:8080/tickets ^ -H "Authorization: Bearer YOUR_TOKEN_HERE"
   ```

## JWE Authentication Mechanism

This application uses JWE (JSON Web Encryption) for securing tokens:

1. **Token Generation** (`JweUtil.generateToken`):
    - Creates a token with user ID, role, issue time, and expiration
    - Encrypts the payload using Direct Encryption with AES-GCM

2. **Token Validation** (`JweUtil.validate`):
    - Decrypts the token using the same shared secret
    - Validates expiration time
    - Extracts user ID and role

3. **Authentication Filter** (`JweAuthenticationFilter`):
    - Intercepts requests with Bearer tokens
    - Validates tokens and creates Spring Security Authentication objects
    - Adds appropriate authorities based on the user's role

4. **Security Configuration** (`SecurityConfig`):
    - Configures stateless sessions
    - Permits access to login endpoint
    - Requires authentication for all other endpoints

## API Endpoints

- `POST /auth/login` - Authenticate and get token
- `POST /tickets` - Create a new ticket
- `GET /tickets` - List tickets (filtered by user role)

## Database

PostgreSQL database is provisioned automatically with Docker Compose:
- Name: sporty
- Username: sporty
- Password: sporty
- Port: 5432

Data persists between container restarts thanks to the Docker volume configuration.

## AI Usage

This project utilized AI assistance in the following ways:

- Generated comprehensive documentation for all classes
- Created the initial configuration for the JWE token implementation, which was then refactored
- Helped improve security practices in the authentication mechanism
- Generated Docker-related files (Dockerfile and docker-compose.yml
- Created initial version of this README.md file

Tools used:
- GitHub Copilot (Claude Sonnet 3.7 model)