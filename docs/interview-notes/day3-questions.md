# Interview Questions, Answers, Mistakes & Lessons

## Overview
focused on transforming a synchronous API into a **production-grade asynchronous job processing system** with persistent lifecycle tracking and structured results.

---

## 1. What problem were you solving on Day 3?
**Answer:**  
I converted a blocking code analysis API into an asynchronous, job-based system. The goal was to handle long-running analysis without blocking HTTP requests while still providing reliable job tracking, status polling, and persisted results.

---

## 2. Why did you introduce asynchronous job processing?
**Answer:**  
Code analysis can be slow and unpredictable. Running it synchronously would block API threads and limit scalability. Asynchronous processing allows the API to respond immediately while the analysis runs in the background, similar to CI pipelines and batch systems.

---

## 3. Why do you create the job before starting async processing?
**Answer:**  
The job record is the **source of truth**. Creating it first guarantees:
- A stable job ID
- Reliable status tracking
- Safe linking of findings
- Observability and debuggability

Without this, async processing would have no persistent anchor.

---

## 4. How is job lifecycle tracked?
**Answer:**  
Each job transitions through well-defined states stored in the database:
- `PENDING` – job created
- `RUNNING` – worker started
- `DONE` – analysis completed successfully
- `FAILED` – error occurred

Timestamps (`created_at`, `started_at`, `finished_at`) provide full lifecycle visibility.

---

## 5. Why separate `AnalysisWorker` from `JobService`?
**Answer:**  
They serve different responsibilities:
- `JobService` orchestrates job creation and retrieval for the API
- `AnalysisWorker` performs background processing

This separation keeps HTTP handling fast and background logic isolated.

---

## 6. Why create multiple small services instead of one large service?
**Answer:**  
Each service has a single responsibility:
- `JobService` – orchestration
- `JobPersistenceService` – job state updates
- `FindingPersistenceService` – writing findings
- `FindingQueryService` – reading findings

This avoids side effects, simplifies testing, and makes future extensions easier.

---

## 7. Why not update entities during GET requests?
**Answer:**  
GET requests must be **side-effect free**. Updating entities during reads would violate REST principles and lead to unpredictable behavior. All database writes are restricted to explicit command paths.

---

## 8. Why use domain objects instead of returning JPA entities?
**Answer:**  
Domain objects decouple API responses from persistence concerns. Returning entities directly risks lazy-loading issues, schema coupling, and fragile APIs. Mapping entities → domain → DTO keeps the design clean.

---

## 9. Why store findings in a separate table?
**Answer:**  
Each job can produce multiple findings. A separate `analysis_finding` table supports a one-to-many relationship, enables filtering and severity analysis, and scales naturally as analysis complexity grows.

---

## 10. Why are findings missing for older jobs?
**Answer:**  
Findings are persisted by the worker. Jobs created before the worker logic existed were already completed without persisting findings. This mirrors real production deployments where new features apply only to new executions.

---

## 11. How does the system ensure consistency?
**Answer:**  
Consistency is guaranteed by:
- Creating the job first
- Persisting findings before marking the job `DONE`
- Ensuring `DONE` implies results exist

This creates a clear data contract.

---

## 12. What improvements were made from Day 2 to Day 3?
**Answer:**  
Day 2 focused on persistence and migrations.  
Day 3 added:
- Asynchronous execution
- Job lifecycle management
- Structured analysis results
- Separation of read/write paths
- Production-style architecture

---

## 13. What is the biggest architectural lesson from Day 3?
**Answer:**  
Long-running work should never block APIs. Systems should persist intent, process asynchronously, expose state via polling, and treat the database as the source of truth.

---

# Common Mistakes & Lessons Learned

## 14. What happens if `@Service` is missing?
**Problem:**  
Spring does not register the class as a bean.

**Symptom:**
**Lesson:**  
Dependency injection depends entirely on proper annotations and package scanning.

---

## 15. What happens if `@EnableAsync` is missing?
**Problem:**  
Async methods execute synchronously.

**Lesson:**  
Annotations enable framework behavior. Without them, async logic is ignored.

---

## 16. What happens if async logic runs before job creation?
**Problem:**  
Findings reference non-existent jobs.

**Lesson:**  
Always persist the job before launching async processing.

---

## 17. What happens if entities are updated in GET requests?
**Problem:**  
Side effects during reads.

**Lesson:**  
GET must be read-only. Writes belong only in command paths.

---

## 18. What happens if Flyway migrations are misnamed?
**Problem:**  
Migrations are silently skipped.

**Lesson:**  
Flyway strictly enforces naming conventions (`V1__description.sql`).

---

## 19. What happens if database drivers are missing?
**Problem:**  
Datasource fails to initialize.

**Lesson:**  
Database drivers must be explicitly included as dependencies.

---

## 20. What happens if tool versions are incompatible?
**Problem:**  
Application fails at startup.

**Lesson:**  
Spring Boot, Flyway, and database versions must be aligned.

---

## 21. What happens if async exceptions are not handled?
**Problem:**  
Jobs remain stuck in `RUNNING`.

**Lesson:**  
Async workers must always handle exceptions and mark jobs as `FAILED`.

---

## 22. What happens if DTO constructor arguments don’t match?
**Problem:**  
Compilation failure.

**Lesson:**  
Java records enforce strict API contracts, preventing runtime bugs.

---

## Final Interview One-Liner
> “On Day 3, I built an asynchronous job-processing pipeline with persistent lifecycle tracking and structured results, similar to how CI or code-review systems operate.”

---
