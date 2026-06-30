# NexusHR Backend (Day 1 — Project Setup)

Spring Boot backend for the NexusHR project (Zidio Development internship).

## Day 1 progress
- Project scaffolded (Maven, Spring Boot 3.3)
- `Employee` and `Department` entities created
- Connected to Neon PostgreSQL
- Basic test endpoints to confirm DB read/write works

## How to run locally
1. Open this folder in IntelliJ / Eclipse / VS Code as a Maven project
2. Edit `src/main/resources/application.properties` with your own Neon DB url/username/password
3. Run `NexusHrBackendApplication.java` (or `mvn spring-boot:run`)
4. Visit `http://localhost:8080/api/health` — should return status UP
5. Test `http://localhost:8080/api/departments` (GET) and POST a department via Postman

## Tech stack (Day 1)
Java 17, Spring Boot 3.3, Spring Data JPA, PostgreSQL (Neon), Lombok, Maven

## Coming up
- Day 2: Spring Security + JWT auth
- Day 3: Full Employee CRUD + validation
