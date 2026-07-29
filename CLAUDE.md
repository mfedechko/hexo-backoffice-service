# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

Requires a local Postgres, started via `docker compose up -d` (see `docker-compose.yml`; db `backoffice`, user/pass `backoffice_user`/`backoffice_pass`, exposed on `5432`). The app connects to it via the `local` Spring profile (default active profile).

```bash
# Build (compiles + runs tests)
mvn clean install

# Compile only
mvn compile

# Run the app (local profile, Postgres via docker-compose must be up)
mvn spring-boot:run

# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=<TestClassName>

# Run a single test method
mvn test -Dtest=<TestClassName>#<testMethodName>
```

Swagger UI is available at `/swagger-ui.html` (OpenAPI JSON at `/v3/api-docs`) once the app is running.

Note: `src/test/java` currently has no test sources at all (just empty package directories) — `mvn test` trivially passes. There is no working example test to copy from; when adding the first one, `spring-boot-starter-test` is already on the classpath.

## Architecture

Single-module Spring Boot 4.1 / Java 25 / Spring Security 7.1 REST service, package root `com.gpn`. Four feature packages share the same app: `com.gpn.leads` (public lead-intake API for a landing page), `com.gpn.auth` (JWT-protected backoffice auth layer with DB-backed RBAC — security config, JWT issuing/validation, and RBAC entities/repositories all live here since they're used app-wide, not just by `leads`), `com.gpn.crm` (KeyCRM-backed product/category/stock/warehouse/report endpoints for the backoffice), and `com.gpn.loghistory` (append-only audit trail written to by other features and exposed as its own read endpoint).

**Package layout** (flat, no further sub-packages within a feature): each feature package has its own `config/`, `controller/`, `model/` (+ `model/dto/`), `repository/`, `security/`, `service/` as needed. `com.gpn.leads` additionally has `exception/`, `mapper/`, `web/` (request logging) and hosts the app-wide `GlobalExceptionHandler` and `OpenApiConfig`. `com.gpn.crm` has its own sub-feature packages (`category/`, `product/`, `stock/`, `warehouse/`, `report/`, `keycrm/` for the upstream client) plus `config/` (KeyCRM `RestClient`/properties) and `web/` (`ApiExceptionHandler` for upstream KeyCRM failures). `com.gpn.loghistory` follows the same `controller/`/`mapper/`/`model/`(`+model/dto/`)/`repository/`/`service/` shape as `leads`.

### Schema & migrations (Flyway, `src/main/resources/db/migration/`)

- `V1__auth_rbac_schema.sql` — `permissions`, `roles`, `roles_permissions`, `users`, `users_roles`. Seeds baseline roles/permissions and a default `admin`/`admin123` user (`ROLE_ADMIN`).
- `V2__add_leads_table.sql` — `leads` table.
- `V3__add_log_history_table.sql` — `log_history` table (see `com.gpn.loghistory` below), FK'd to `users`.

`spring.jpa.hibernate.ddl-auto=validate` — Flyway owns the schema; Hibernate only validates entities match it. New columns/tables require a new `V{n}__*.sql` migration, not entity changes alone.

### Auth / RBAC (`com.gpn.auth`)

- `UserEntity` (`model/UserEntity.java`) implements `UserDetails` directly. `getAuthorities()` returns the union of the user's role names (e.g. `ROLE_ADMIN`, for `hasRole`/`@PreAuthorize`) and the permission names gathered transitively from those roles (e.g. `leads:write`, for `hasAuthority`). `UserRepository.findByUsername` uses `@EntityGraph` to eager-fetch `roles` and `roles.permissions` in one query — required because the `@ManyToMany` associations are `LAZY` and there's no service-layer transaction wrapping the auth flow.
- `JwtService` (`security/`) issues/validates HMAC-signed JWTs (`jjwt` 0.12 builder API) and embeds `roles` and `permissions` as claims at generation time. `extractAuthorities()` rebuilds `GrantedAuthority`s straight from those claims — token validation does **not** hit the DB.
- `JwtAuthenticationFilter` (`OncePerRequestFilter`) reads `Authorization: Bearer <token>`, validates it via `JwtService`, and populates `SecurityContextHolder` with the authorities extracted from the token claims.
- `SecurityConfig` (`config/`) lives here rather than in `leads` because it governs the whole app, not just the leads feature: stateless sessions, CSRF disabled, `@EnableMethodSecurity` on. `AuthenticationManager` is a `ProviderManager` wrapping a `DaoAuthenticationProvider(userDetailsService)` with `setPasswordEncoder(...)` — Spring Security 7.1 removed the `DaoAuthenticationProvider(PasswordEncoder)` constructor and `setUserDetailsService`, so this is the current non-deprecated construction pattern (don't "fix" it back to the old 6.x shape). `/api/auth/**` and Swagger paths are `permitAll`; `POST /api/leads` is `permitAll`; everything else (including all of `com.gpn.crm`) requires authentication via the blanket `anyRequest().authenticated()` — new authenticated endpoints don't need an explicit matcher added.
- `AuthController.login` authenticates through `AuthenticationManager` (not manual password comparison) and returns a `LoginResponse` record with the token plus username/email/roles.
- `LoginRequest`/`LoginResponse` are Java records (`model/dto/`), unlike the Lombok `@Getter/@Setter` DTOs used elsewhere in the codebase (e.g. `CreateLeadRequest`, `LeadResponseDto`) — records are the intended pattern going forward for new DTOs.

### Leads feature (`com.gpn.leads`)

Straightforward CRUD-ish flow: `LeadController` → `LeadService` (`@Transactional` per method) → `LeadRepository` (Spring Data JPA), with `LeadMapper` (a static `@UtilityClass`, not a bean) converting `LeadEntity` → `LeadResponseDto`. `POST /api/leads` is intentionally `permitAll` (public landing-page submission endpoint); `GET /api/leads` and `GET /api/leads/{id}` require auth. `LeadEntity` sets `createdAt`/`updatedAt` via `@PrePersist`/`@PreUpdate`.

### CRM feature (`com.gpn.crm`)

Read-only backoffice views over KeyCRM data, all requiring authentication (no explicit security matchers needed — see above). `category/`, `product/`, `stock/`, `warehouse/`, `report/` each follow controller → service → (mapper) against `keycrm/client/*` (`RestClient`-based clients, one per KeyCRM resource) and `keycrm/dto/*` (raw KeyCRM API shapes, mapped to this service's own DTOs before leaving the service layer). `config/KeyCrmClientConfig` builds the shared `RestClient` bean and fails fast at startup if `keycrm.api-token` is unset or still contains a literal `${...}` placeholder (Spring's relaxed binding doesn't fail on an unresolved env var by itself). `keycrm.base-url`/`keycrm.api-token` are set once in the shared (non-profile-specific) section of `application.yaml` — don't move them under a single profile's `---` document, since that silently breaks the KeyCRM client under the other profile(s). `web/ApiExceptionHandler` translates upstream `RestClientResponseException`/`ResourceAccessException` into passthrough 4xx or 502/504 responses for callers.

KeyCRM has no endpoint that returns warehouses directly, nor one that lists offers with stock at zero: `warehouse/WarehouseService` derives the warehouse list by paging through the full `/offers/stocks` catalog and deduping the warehouse id/name nested in each item; `report/WarehouseStockReportService` builds its per-warehouse Excel export (`report/excel/WarehouseStockExcelWriter`) by joining three separate full-catalog scans (`/products`, `/offers`, `/offers/stocks`). Both patterns are the correct way to answer "give me all X, including zero-stock/unlisted ones" against this API — don't assume a missing item means a missing KeyCRM endpoint. `WarehouseStockReportService` calls `loghistory.LogHistoryService.logReportGeneration(...)` after generating each report, so `com.gpn.crm` depends on `com.gpn.loghistory` (not the other way around).

### Log history (`com.gpn.loghistory`)

Generic, append-only audit trail (`log_history` table, `V3__add_log_history_table.sql`) — not specific to any one feature. `LogHistoryEntity` records `userId`, a `module` enum (`LogHistoryModule`: `REPORT`, `LEAD`, `USER`), a free-text `action` (mirrors `LogHistoryAction`: `GENERATE`, `ADD`, `REMOVE`, `UPDATE`, but the column/mapper treat it as a plain string, not a JPA `@Enumerated`), an `objectId` string, and a `details` `jsonb` column mapped to `Map<String, String>`. Entries are read via `GET /api/activity-logs`, filtered through `LogHistorySpecification` (a `JpaSpecificationExecutor` spec built from `LogHistoryFilterRequest`) — note the endpoint path (`activity-logs`) and the table/package name (`log_history`/`loghistory`) diverge, a holdover from the feature's original "activity log" naming. There's currently one write path, `LogHistoryService.logReportGeneration`, called from `crm.report.WarehouseStockReportService` and reading the acting user via `auth.security.AuthDetailsHolder.getCurrentUser()`; new call sites should follow that pattern (resolve the user from `AuthDetailsHolder`, don't thread it through as a parameter).

### Error handling

`GlobalExceptionHandler` (`@RestControllerAdvice`) centralizes exception → HTTP status mapping: `MethodArgumentNotValidException` → 400 with a `field: message` summary, `LeadNotFoundException` → 404, `BadCredentialsException` → 401. Add new domain exceptions here rather than handling them per-controller.

### Config (`application.yaml`)

Single YAML with `local`/`prod` profile documents (`---` separated). Active profile defaults to `local`. JWT secret/expiry and CORS allowed origin are externalized via `app.*` properties with env var overrides (`JWT_SECRET`, `JWT_EXPIRATION_MINUTES`); prod datasource credentials come from `DB_USER`/`DB_PASSWORD`.
