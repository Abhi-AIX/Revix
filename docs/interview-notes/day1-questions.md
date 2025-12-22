
## 1. Why did we use records instead of classes for DTOs?

**Answer:** DTOs are immutable data carriers, and Java records are purpose-built for that role.

- **Less boilerplate**: auto-generated constructor, getters, `equals/hashCode`, and `toString`
- **Immutable by default**: safer for APIs; prevents accidental mutation
- **Clear intent**: signals “this is data, not behavior”
- **Framework-friendly**: Spring/Jackson deserialize records cleanly; validation annotations work on record components

**When not to use records**
- When mutable fields are required
- When a no-args constructor is needed
- When complex inheritance is necessary (records cannot extend classes)

---

## 2. Why create DTOs at all? Why not use `AnalysisJob` directly in the controller?

**Answer:** Separation of concerns and API safety.

- **Stable API contract**: DTOs define the public interface; internal models can evolve
- **Security**: domain models may contain fields that must not be exposed (tokens, payloads, internal errors)
- **Validation at boundaries**: DTOs hold validation rules without polluting the domain model
- **Loose coupling**: domain changes don’t automatically become breaking API changes

---

## 3. Why have a `JobService` instead of putting logic in the controller?

**Answer:** Controllers should be thin; business logic belongs in services.

- **Testability**: services can be unit-tested without the web layer
- **Maintainability**: controllers handle HTTP concerns; services handle business logic
- **Future-proofing**: persistence or async changes don’t require controller rewrites
- **Clean architecture**: the web layer should not own domain rules

> **Interview note:** “Fat controllers are a code smell.”

---

## 4. Why use an in-memory `ConcurrentHashMap` on Day 1?

**Answer:** A deliberate Agile vertical slice.

- **Fast feedback loop**: validate API flow before adding infrastructure
- **Low setup friction**: no database required
- **Thread safety**: prepares for async job processing
- **Easy replacement**: storage is abstracted; can be swapped with Postgres later without API changes

> **Interview framing:**  
> “Persistence is decoupled behind a service so it can evolve without breaking endpoints.”

---

## 5. Why choose a job-based API (returning `jobId`) instead of returning analysis immediately?

**Answer:** It scales better and aligns with future requirements.

- GitHub webhooks must respond quickly
- LLM calls are slow and unpredictable

The job-based model supports:
- asynchronous processing
- retries
- status tracking
- audit and history

One design works for both:
- paste-code review (Phase 1)
- PR review via webhooks (Phase 2)

> **Interview line:**  
> “I designed for eventual async execution from day one to avoid a later redesign.”

---

## 6. Why use `@Valid` and `@NotBlank` at the API boundary?

**Answer:** Defensive design and clean layering.

- **Fail fast**: invalid requests return clear 400 responses
- **Clean domain**: service layer assumes validated input
- **Consistency**: centralized validation mechanism
- **Security & cost control**: avoids unnecessary LLM calls with empty or garbage input
