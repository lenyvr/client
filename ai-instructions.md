# Project Instructions and Coding Guidelines

You are an expert programming assistant in Java 21, Spring Boot, and Hexagonal Architecture. Your goal is to help develop this microservice while maintaining a strict separation of concerns.

## 1. Technology Stack
- **Language:** Java 21 (Modern features: Records, Pattern Matching, Switch Expressions).
- **Framework:** Spring Boot 3.x
- **Database:** PostgreSQL
- **Dependency Manager:** Gradle with Kotlin DSL (`build.gradle.kts`).

## 2. Hexagonal Architecture (Strict Rules)
The project is strictly divided into three layers. **All directory, package, and file names must be written in English.** **No internal layer may depend on an external layer.**

### A. Domain Layer (`domain`)
- **Content:** Business entities, Value Objects, business exceptions, and Port interfaces.
- **GOLDEN RULE:** Zero external dependencies. It is **FORBIDDEN** to import Spring, JPA, Hibernate, Jackson, Lombok, or any infrastructure library.
- Output Ports are interfaces that define how the domain communicates with the outside world (e.g., `UserRepository`).

### B. Application Layer (`application`)
- **Content:** Use Cases (Services) and Input Port interfaces.
- **Strict Rules:**
    - It is **FORBIDDEN** to use Spring annotations (`@Service`, `@Component`, `@Autowired`, `@Value`, etc.). The application layer must be pure Java.
    - Use Cases are standard Java classes (POJOs) that receive their dependencies (Ports) exclusively through the constructor.

### C. Infrastructure Layer (`infrastructure`)
- **Content:** Adapters (REST, JPA), controllers, Spring configurations, and output adapters.
  - **To have present**: 
    - Endpoints must have API documentation (Swagger, OpenAPI).
    - Error handling must be centralized in this layer using `@ControllerAdvice`.
    - Logging must be centralized in this layer using `@Slf4j`.
- **Dependency Injection and Beans:**
    - **MANDATORY RULE:** All Use Cases from the `application` layer must be registered as Spring Beans within this infrastructure layer.
    - One or more configuration classes must be created in `infrastructure` (for example, in an `infrastructure.config.BeanConfiguration` package) annotated with `@Configuration`.
    - Inside this class, methods annotated with `@Bean` must be defined to manually instantiate the application Use Cases, passing the required port implementations (infrastructure) to them.

## 3. Code Style and Best Practices
- **Immutability:** Prefer using `record` for DTOs and Value Objects if they do not require mutability.
- **Dependency Injection:** Always prefer constructor injection. Do not use `@Autowired` on fields.
- **Error Handling:** Centralized in the infrastructure layer using `@ControllerAdvice`, translating domain exceptions into appropriate HTTP responses.
- **Naming Conventions:**
    - Output Ports: `[Name]SPI`.
    - Use Cases: `[Action][Entity]UseCase`.
    - Controllers: `[Name]Controller`

## 4. Code Generation Workflow
When I ask you to create a new feature, always proceed in this exact order or ask me before moving forward:
1. Create/Modify the model in `domain`.
2. Create the necessary Ports (Interfaces) in `domain`.
3. Implement the Use Case in `application`.
4. Implement the adapters (Controller, JPA Entity, Repository) in `infrastructure`.

## 5. Bean Configuration Example (Reference)

When you create a Use Case, remember that a configuration class similar to this one must exist in `infrastructure`:

```java
package com.tuempresa.microservicio.infrastructure.config;

import com.tuempresa.microservicio.application.usecases.CreateUserUseCase;
import com.tuempresa.microservicio.domain.ports.output.UserRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepositorySPI userRepositorySPI) {
        return new CreateUserUseCase(userRepositorySPI);
    }
}
```
## 6. Development Environment and Containerization

The project is fully containerized and managed with Gradle (Kotlin DSL). The assistant must respect the following configurations when creating scripts, properties, or new modules:

### A. Dependency Management (Gradle Kotlin DSL)
- Any new dependency must be added to build.gradle.kts using Kotlin syntax (e.g., implementation("org.springframework.boot:spring-boot-starter-data-jpa")).
- It is **FORBIDDEN** to generate configuration blocks in Groovy or Maven (pom.xml) format.

### B. Docker and Database Architecture
The project has a multi-container environment managed by `docker-compose.yml`:
- **Database Service**: PostgreSQL (Check the environment variables in `docker-compose.yml` for credentials, database name, and port before suggesting changes to `application.yml).
- **Application Service**: Built from the project's `Dockerfile`, which compiles the JAR using Gradle and runs it.

### C. Key Project Commands
When suggesting terminal commands, exclusively use:
- **Compile/Build:** `./gradlew clean build`
- **Spin up full environment:** `docker-compose up --build`
- **Spin up database only:** `docker-compose up   pizzaioli-cost-db`

## 7. Version Control (Git)

The project uses Git for version control. The assistant must strictly adhere to the following rules when suggesting or executing Git commands, creating branches, or writing commits.

### A. Commit Message Standard (Conventional Commits)
All commits must follow the structure: `<type>(<scope>): <short description in lowercase>`.
- **Allowed Types:**
    - `feat`: A new feature (e.g., `feat(user): add SPI output port`).
    - `fix`: A bug fix (e.g., `fix(database): fix connection url in yml`).
    - `refactor`: Code changes that neither fix a bug nor add a feature (e.g., `refactor(application): move logic to value object`).
    - `docs`: Documentation-only changes (e.g., `docs(readme): update docker guide`).
    - `chore`: Maintenance tasks, updating Gradle dependencies, etc.
- **Language:** Commit messages and branch names must be written in English

### B. Branching Strategy
- The primary branch is `main`. It is **forbidden** to suggest direct changes to it when developing a new feature.
- Las ramas de trabajo deben seguir la nomenclatura: `feature/[id-tarea]-[descripcion-corta]` o `bugfix/[id-tarea]-[descripcion]`.
- Working branches must follow the nomenclature: `feature/[task-id]-[short-description]` or `bugfix/[task-id]-[description].
    - *Example:* `feature/TASK-04-register-user`.