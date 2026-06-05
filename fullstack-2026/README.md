# 🚀 Fullstack 2026 - Proyecto con Tecnologías Modernas

## Arquitectura

```
┌─────────────────────────────────────────────────────────────────┐
│                      USUARIO (Navegador)                         │
└──────────────────────────────┬──────────────────────────────────┘
                               │ HTTPS
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                    NGINX (Reverse Proxy + SSL)                    │
│  /           → Frontend Angular 21 (Signals, Zoneless)           │
│  /api/       → Backend Spring Boot 3.3 (Java 25, Virtual Threads)│
└──────────────────────────────┬──────────────────────────────────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        ▼                      ▼                      ▼
┌──────────────┐    ┌──────────────────┐    ┌──────────────────┐
│  PostgreSQL  │    │      Redis       │    │   Keycloak       │
│  (BD única)  │    │  (Caché/Sesión)  │    │ (Identity Prov.) │
│  :5432       │    │  :6379           │    │  :8180           │
└──────────────┘    └──────────────────┘    └──────────────────┘
```

## Stack Tecnológico 2026

### Backend
- Java 25 (LTS) — Virtual Threads, Records, Pattern Matching, Sealed Classes
- Spring Boot 3.3 — Virtual Threads habilitados, Observability nativa
- Spring Security 6 + OAuth2 Resource Server
- Spring Data JPA + PostgreSQL
- Spring AI — Integración con LLMs
- OpenTelemetry — Traces distribuidos
- GraalVM Native Image — Arranque en milisegundos
- Testcontainers — Tests con BD real

### Frontend
- Angular 21 — Signals, Zoneless, @defer, control flow nativo
- TypeScript 5.5 strict
- Angular Material 21
- TanStack Query (Angular Query) — Server state management
- Playwright — E2E testing

### Infraestructura
- PostgreSQL 16 — BD relacional principal
- Redis 7 — Caché y rate limiting
- Keycloak 25 — Identity Provider (OAuth2/OIDC)
- Docker + Docker Compose
- Kubernetes + ArgoCD (producción)
- GitHub Actions (CI/CD)

### ¿Por qué este stack?

| Decisión | Razón 2026 | Alternativa |
|----------|------------|-------------|
| PostgreSQL único | Más simple que 3 BDs, JSON nativo, extensible | MySQL, CockroachDB |
| Keycloak | No reinventar auth, MFA gratis, social login | Auth0 (SaaS), Supabase Auth |
| Virtual Threads | 100K+ requests sin reactive complexity | WebFlux (más complejo) |
| Records | DTOs inmutables sin Lombok | Lombok (legacy) |
| Angular Signals | Rendimiento sin Zone.js, reactividad granular | React + Next.js |
| Redis | Caché + rate limit + sesiones en un solo servicio | Caffeine (solo local) |
| OpenTelemetry | Estándar de observabilidad, vendor-neutral | Prometheus solo (limitado) |
