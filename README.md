# Client Microservice

Customer management microservice for a fintech platform. Implements hexagonal architecture (Ports & Adapters) with asynchronous communication via RabbitMQ to the accounts microservice.

## Tech Stack

| Component | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Build | Gradle 8 (Kotlin DSL) |
| Database | PostgreSQL 18 |
| Messaging | RabbitMQ 3.13 |
| API Documentation | Springdoc OpenAPI (Swagger UI) |
| Containers | Docker / Docker Compose |

## Architecture

The project follows **hexagonal architecture** with three strictly separated layers:

```
client/
├── domain/          # Entities, value objects, ports (no external dependencies)
├── application/     # Use cases and business logic (pure POJOs)
└── infrastructure/  # Spring Boot, REST controllers, JPA, RabbitMQ
```

- **Domain:** zero external dependencies; defines contracts through ports (SPI/API).
- **Application:** orchestrates use cases; no Spring annotations.
- **Infrastructure:** adapts the domain to concrete technologies (HTTP, database, messaging).

## Prerequisites

- Docker and Docker Compose
- Java 21 (only for local development without Docker)

## Environment Variables

Create a `.env` file at the project root based on the following template:

```env
DB_NAME=client_db
DB_USER=<db_user>
DB_PASSWORD=<db_password>
APP_PORT=8080
RBQ_HOST=devsu-client-queue
RBQ_PORT=5672
RBQ_USER=<rabbitmq_user>
RBQ_PASSWORD=<rabbitmq_password>
```

## Running the Project

### With Docker (full environment)

```bash
docker-compose up --build
```

This starts three services:
- **App** — http://localhost:8080
- **PostgreSQL** — localhost:5432
- **RabbitMQ Management** — http://localhost:15672

### Local Development

```bash
# Start dependencies only, you must verify if the account microservice haven't been started the queue first to avoid conflicts
docker-compose up devsu-client-db devsu-client-queue

# Run the app with auto-reload
./gradlew :infrastructure:bootRun -t
```

### Build

```bash
./gradlew clean build
```

## API Documentation

With the application running, access Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

## Data Model

### Main Entities

**Person**

| Field | Type | Description |
|---|------------------|-------------------------------|
| `person_id` | BIGINT (PK) | Internal identifier |
| `first_name` | VARCHAR | First name |
| `last_name` | VARCHAR | Last name |
| `age` | INTEGER | Age |
| `gender_id` | int4 (FK) | 1(F) / 2(M) |
| `identification_type_id` | INT4 (FK) | 1(DNI) / 2(NIF) / 3(PPT) |
| `identification_number` | VARCHAR (UNIQUE) | Document number |
| `address` | VARCHAR | Address |
| `postal_code` | VARCHAR | Postal code |
| `contact_number` | VARCHAR | Phone number |
| `email` | VARCHAR | Email address (optional) |

**Client** (extends Person)

| Field | Type | Description |
|---|---|---|
| `client_id` | BIGINT (PK) | Internal identifier |
| `person_id` | BIGINT (FK) | Reference to Person |
| `client_code` | VARCHAR (UNIQUE) | Client code |
| `password` | VARCHAR | Password (encrypted) |
| `status` | BOOLEAN | true = active, false = inactive |

## Communication with the Accounts Microservice

This service communicates asynchronously with the accounts microservice via RabbitMQ:

| Exchange / Queue | Direction | Purpose |
|---|---|---|
| `accounts.exchange` | — | Main exchange |
| `accounts.check-request` | Outbound | Check if the client has open accounts |
| `client.deactivation-response` | Inbound | Response before deactivating a client |

## Development Conventions

- **Branches:** `feature/<id>-<description>` / `bugfix/<id>-<description>`
- **Commits:** Conventional Commits format (`feat(scope): description`)
- **Output ports (SPI):** name ends in `SPI`
- **Use cases:** pattern `[Action][Entity]UseCase`
