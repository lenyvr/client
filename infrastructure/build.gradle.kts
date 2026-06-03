plugins {
    java
    id("org.springframework.boot") version "3.5.14"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.devsu.fintech.infrastructure"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))

    // Web (REST controllers)
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Bean Validation for request DTOs
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Persistence (JPA adapters)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // Messaging (RabbitMQ - see docker-compose)
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    // PostgreSQL driver (runtime only)
    runtimeOnly("org.postgresql:postgresql")

    // API documentation (Swagger / OpenAPI)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")

    // Lombok for centralized logging (@Slf4j) and adapter boilerplate
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<Test> {
    useJUnitPlatform()
}