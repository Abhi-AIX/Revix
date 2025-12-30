# Interview Notes (Analyzer Routing + Metadata)

## What we achieved today?
Upgraded Revix from a single-analysis implementation into a multi-analyzer platform by:
- Introducing `AnalyzerRouter` to centralize analyzer selection
- Persisting analyzer metadata (`analyzer_type`, `analyzer_version`) per job
- Returning analyzer metadata in the job status API response
  This enables future AI integration without changing the job workflow.

---

## 1) Why store `analyzerType` and `analyzerVersion` on every job?
Analyzer metadata is part of job identity and traceability.
- **Auditability:** We can prove which analyzer ran and with which implementation.
- **Reproducibility:** If results change later (rules-v2), we can compare old jobs.
- **Debugging:** Faster RCA when users report “why did it flag this?”
- **Analytics:** Dashboard can show outcomes per analyzer/version.

This is how production systems avoid “silent behavior changes” after deployments.

---

## 2) Why is `analyzerType` an enum but `analyzerVersion` a String?
- `analyzerType` is a finite set of strategies (`RULES`, `AI`) and directly drives routing → enum is ideal.
- `analyzerVersion` evolves frequently (`rules-v1`, `rules-v2`, `gpt-4o`, etc.) and should be flexible → String is best.
  Making version an enum would force code changes for every release and is not scalable.

---

## 3) Why did we introduce `AnalyzerRouter` instead of injecting an analyzer directly into the worker?
Direct injection couples the worker to one analyzer implementation.
Router centralizes selection logic, enabling:
- Plug-and-play analyzers (RULES now, AI later)
- Cleaner worker (orchestration only)
- A single place to validate supported analyzer types
- Future feature flags / per-language mapping

This follows the Dependency Inversion Principle: worker depends on abstractions, not concretes.

---

## 4) Why should `JobService` normalize analyzerType instead of the controller?
Controllers should be thin and not contain business rules.
Analyzer selection is part of job creation semantics (defaults, validation), so it belongs in `JobService`.
This keeps HTTP concerns separate from core logic and makes the service reusable for future APIs (webhooks, CLI, etc.).

---

## 5) Why did Postgres throw “null value violates not-null constraint” even though the DB column had a default?
Hibernate will include columns in INSERT statements using entity field values.
If an entity field is `null`, Hibernate may insert `NULL` explicitly, bypassing DB defaults.
Fix: set defaults in the entity and/or assign analyzer fields before `repo.save(entity)`.

Interview keyword: “ORMs may bypass database defaults unless configured.”

---

## 6) Why return analyzer metadata in the GET response?
This makes the API self-describing:
- Clients can display which analyzer ran
- Enables front-end dashboards and logs
- Prevents ambiguity when multiple analyzers exist
- Supports later “re-run with AI” or comparisons

---

## 7) What would go wrong if analyzerType wasn’t passed into the worker?
We would persist one analyzerType in DB but execute a different analyzer in runtime.
That breaks correctness and auditability (DB truth ≠ runtime behavior) and makes debugging nearly impossible.

---

## 8) Common mistakes & failure modes (real examples)
- Missing `@Service` / bean not found → Spring injection fails at startup
- Relying on DB defaults with Hibernate → NULL inserts break NOT NULL constraints
- Performing `setX()` inside constructor args → setters return void; mapping belongs after construction
- Putting entity logic in controller → leaks persistence concerns to API layer

---

## One-line summary (interview-ready)
“Day 5 made Revix multi-analyzer by adding analyzer routing + persisted analyzer metadata per job, ensuring traceability and enabling future AI analyzers without refactoring the execution pipeline.”
