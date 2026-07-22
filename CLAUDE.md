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
mvn test -Dtest=LeadEntityControllerTest

# Run a single test method
mvn test -Dtest=LeadEntityControllerTest#postLead_validPayload_returns201
```

Swagger UI is available at `/swagger-ui.html` (OpenAPI JSON at `/v3/api-docs`) once the app is running.

Note: `mvn test` currently fails to *compile* the test sources in this environment (`LeadEntityControllerTest` — `spring-boot-test-autoconfigure` / `AutoConfigureMockMvc` not resolving from the local `.m2` cache). This is pre-existing and unrelated to application code changes; verify behavior by running the app and hitting endpoints with `curl` when the test module won't compile.

## Architecture

Single-module Spring Boot 4.1 / Java 25 / Spring Security 7.1 REST service, package root `com.gpn`. Three feature packages share the same app: `com.gpn.leads` (public lead-intake API for a landing page), `com.gpn.auth` (JWT-protected backoffice auth layer with DB-backed RBAC — security config, JWT issuing/validation, and RBAC entities/repositories all live here since they're used app-wide, not just by `leads`), and `com.gpn.crm` (KeyCRM-backed product/category/stock/report endpoints for the backoffice).

**Package layout** (flat, no further sub-packages within a feature): each feature package has its own `config/`, `controller/`, `model/` (+ `model/dto/`), `repository/`, `security/`, `service/` as needed. `com.gpn.leads` additionally has `exception/`, `mapper/`, `web/` (request logging) and hosts the app-wide `GlobalExceptionHandler` and `OpenApiConfig`. `com.gpn.crm` has its own sub-feature packages (`category/`, `product/`, `stock/`, `report/`, `keycrm/` for the upstream client) plus `config/` (KeyCRM `RestClient`/properties) and `web/` (`ApiExceptionHandler` for upstream KeyCRM failures).

### Schema & migrations (Flyway, `src/main/resources/db/migration/`)

- `V1__auth_rbac_schema.sql` — `permissions`, `roles`, `roles_permissions`, `users`, `users_roles`. Seeds baseline roles/permissions and a default `admin`/`admin123` user (`ROLE_ADMIN`).
- `V2__add_leads_table.sql` — `leads` table.

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

Read-only backoffice views over KeyCRM data, all requiring authentication (no explicit security matchers needed — see above). `category/`, `product/`, `stock/`, `report/` each follow controller → service → (mapper) against `keycrm/client/*` (`RestClient`-based clients, one per KeyCRM resource) and `keycrm/dto/*` (raw KeyCRM API shapes, mapped to this service's own DTOs before leaving the service layer). `config/KeyCrmClientConfig` builds the shared `RestClient` bean and fails fast at startup if `keycrm.api-token` is unset or still contains a literal `${...}` placeholder (Spring's relaxed binding doesn't fail on an unresolved env var by itself). `keycrm.base-url`/`keycrm.api-token` are set once in the shared (non-profile-specific) section of `application.yaml` — don't move them under a single profile's `---` document, since that silently breaks the KeyCRM client under the other profile(s). `web/ApiExceptionHandler` translates upstream `RestClientResponseException`/`ResourceAccessException` into passthrough 4xx or 502/504 responses for callers.

### Error handling

`GlobalExceptionHandler` (`@RestControllerAdvice`) centralizes exception → HTTP status mapping: `MethodArgumentNotValidException` → 400 with a `field: message` summary, `LeadNotFoundException` → 404, `BadCredentialsException` → 401. Add new domain exceptions here rather than handling them per-controller.

### Config (`application.yaml`)

Single YAML with `local`/`prod` profile documents (`---` separated). Active profile defaults to `local`. JWT secret/expiry and CORS allowed origin are externalized via `app.*` properties with env var overrides (`JWT_SECRET`, `JWT_EXPIRATION_MINUTES`); prod datasource credentials come from `DB_USER`/`DB_PASSWORD`.
