# Agent Status and Work Plan (Logbook)

*IInstructions for the Agent: Keep this file updated. Every time you implement a use case, a business rule, or make a technical decision, record it here. Mark completed tasks with [x].*

## 1. Actual project status
- **Current phase:** Design and implementation of infrastructure.
- **Estructura base:** Already created (packages `domain`, `application`, `infrastructure` configured with Gradle Kotlin DSL and Docker).

## 2. Roadmap and Upcoming Use Cases
*Agent: Break down here the technical steps (ports, use cases, adapters, beans) for the requirements that I will request.*
**Important:** client entity is an extension of a person entity. To save a client, the person entity must be saved first.
- [ ] generate create a client use case.
- [ ] generate an Update client use case.
- [ ] generate a List clients use case; this one must have a filter by name, identification, or client code. 
- [ ] Generate unit tests for each use case.
- [ ] Generate unit test for client and person entity.
- [ ] Generate integration tests for the use cases.

## 3. Technical Decisions Made
*Record here any significant changes to the code, custom exceptions, mappers, or design patterns used.*
- It is confirmed that dependency injection of the layer of `application` It will be managed centrally through a class `@Configuration` in the infrastructure (`BeanConfiguration`), keeping the application clean and organized.

## 4. Notes and Bugs
*Note here if business information is missing, if there is a bug in Docker, or if there are any pending dependencies.*
- None currently. Ready to start use cases.