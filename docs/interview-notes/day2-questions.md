## Topic: Persistence, Flyway, and Production-Ready Foundations

---

## What was built on Day 2
On Day 2, the Revix backend evolved from an in-memory prototype into a **durable, production-grade backend** with:

- PostgreSQL (Docker-based)
- Flyway for schema versioning
- JPA entities + repositories
- Persistent job lifecycle (`PENDING` state stored in DB)
- Clean separation of API, domain, and persistence layers

---

## Why Flyway instead of Hibernate auto table creation?

Hibernate can generate tables automatically using `ddl-auto`, but we intentionally avoided it.

### Why Flyway was chosen
- **Versioned schema**: Each change is tracked (`V1`, `V2`, etc.)
- **Consistency across environments**: Dev, staging, prod get identical schemas
- **Safe deployments**: SQL changes are reviewed like application code
- **Auditability**: `flyway_schema_history` shows exactly what ran and when
- **Predictability**: No surprise schema changes during startup

> “I used Flyway to make schema evolution deterministic and reviewable. Hibernate auto-DDL is convenient for demos, but Flyway is safer for real deployments.”

---

## Why strict folder structure matters

Spring Boot and Flyway rely heavily on conventions.

### Examples
- Flyway scans migrations from `classpath:db/migration`
- Spring Boot loads:
    - `src/main/resources` for runtime
    - `src/test/resources` for tests
- Component scanning depends on package structure

### Benefits
- Zero custom wiring
- Fewer configuration bugs
- Faster onboarding for new developers

> “Following conventions reduces configuration overhead and prevents subtle runtime issues.”

---

## What happens if Flyway is not used?

Without Flyway:
- Manual DB changes drift across environments
- No visibility into schema version
- Risky deployments (“works on my machine”)
- Difficult collaboration when multiple devs change DB

> “Without Flyway, schema changes become undocumented and error-prone, especially as teams scale.”

---

## Problems faced on Day 2 and how they were solved

### 1. Java version mismatch
- **Issue**: Project compiled with Java 21, machine used Java 17
- **Fix**: Installed JDK 21 and aligned Maven + IDE

### 2. Tests failing after DB integration
- **Issue**: Spring context failed during `mvn test`
- **Fix**:
    - H2 added as test-only dependency
    - `src/test/resources/application.yml` created

### 3. Postgres driver not found
- **Issue**: `org.postgresql.Driver` missing at runtime
- **Fix**: Added Postgres dependency with runtime scope

### 4. Flyway didn’t run initially
- **Issues encountered**:
    - Missing Flyway dependency
    - Missing Postgres Flyway module
    - Invalid migration filename
- **Fixes**:
    - Added `spring-boot-starter-flyway`
    - Added `flyway-database-postgresql`
    - Renamed migration to `V1__init.sql`

### Final verification
- Tables created successfully
- `flyway_schema_history` populated
- App started cleanly

> “Most issues were configuration and environment-related. I debugged them by reading logs, validating classpaths, and verifying database state directly.”

---

## What changed from Day 1 to Day 2?

### Day 1 – Prototype
- In-memory storage (`ConcurrentHashMap`)
- No persistence
- No schema or migrations
- Demo-level reliability

### Day 2 – Foundation
- PostgreSQL with Docker
- Flyway-managed schema
- JPA entities + repositories
- Persistent job lifecycle
- Ready for async processing and scaling

> “Day 1 proved the idea. Day 2 made it production-ready by introducing persistence, schema versioning, and infrastructure discipline.”

---

## Why separate Domain models and JPA Entities?

- **Domain models** represent business logic
- **Entities** represent database structure
- Prevents persistence leaking into API logic
- Makes refactoring and schema changes safer

> “I separate domain models from entities so business logic remains independent of persistence concerns.”

---