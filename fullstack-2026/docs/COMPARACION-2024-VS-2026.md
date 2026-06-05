# Comparación: Tu Proyecto Anterior vs Proyecto 2026

## Resumen de Cambios

| Aspecto | Proyecto Anterior (2024) | Proyecto 2026 |
|---------|--------------------------|---------------|
| **Java** | 17 | 25 (LTS) |
| **Spring Boot** | 3.2 | 3.3 |
| **DTOs** | Clases + Lombok (@Data, @Builder) | Records (nativos, inmutables) |
| **Threads** | Platform threads (pool ~200) | Virtual Threads (100K+ concurrentes) |
| **Auth** | JWT custom (AuthService genera tokens) | OAuth2 + Keycloak (IdP externo) |
| **BD** | 3 BDs (MySQL + Oracle + SQL Server) | 1 BD (PostgreSQL con JSONB) |
| **Migraciones** | hibernate.ddl-auto=update | Flyway (versionadas, auditables) |
| **Tests** | H2 in-memory | Testcontainers (BD real en Docker) |
| **GC** | G1GC (default) | ZGC (pausas < 1ms) |
| **Observability** | Actuator + Prometheus | OpenTelemetry (traces + metrics + logs) |
| **Angular** | 17 (Zone.js, *ngIf, subscribe) | 21 (Zoneless, Signals, @if, resource) |
| **State** | Variables + subscribe() | signal() + computed() + rxResource() |
| **Change Detection** | Zone.js (revisa todo) | Signals (solo lo que cambió) |
| **Auth Frontend** | Formulario custom + localStorage | Keycloak JS adapter (redirect flow) |
| **CSS** | SCSS + Angular Material | CSS nativo (nesting, :has) + Material |
| **Build** | Webpack (Angular CLI) | esbuild/Vite (Angular CLI 21) |

## ¿Por qué PostgreSQL en lugar de 3 BDs?

| Razón | Explicación |
|-------|-------------|
| JSONB nativo | Almacena datos flexibles (audit logs) sin necesitar SQL Server |
| Full-text search | `to_tsvector` reemplaza Elasticsearch para búsquedas simples |
| Partial indexes | Índices condicionales (solo productos activos) |
| INET type | Tipo nativo para IPs (no VARCHAR) |
| Simplicidad | 1 conexión, 1 backup, 1 monitoreo |
| Costo | Gratis, sin licencias (Oracle cuesta $$$) |
| Ecosistema | Supabase, Neon, CockroachDB son PostgreSQL-compatible |

## ¿Por qué Keycloak en lugar de JWT custom?

| Aspecto | JWT Custom (antes) | Keycloak (ahora) |
|---------|-------------------|------------------|
| Código auth | ~500 líneas (Service + Filter + Config) | ~50 líneas (solo validar token) |
| MFA | Implementar desde cero | Checkbox en admin console |
| Social login | Implementar cada proveedor | Configurar en UI |
| Password policies | Código custom | Configurar en UI |
| Brute force protection | Rate limiting manual | Built-in |
| Session management | No hay (stateless) | Dashboard completo |
| User management | CRUD manual | Admin console incluida |

## ¿Por qué Signals en lugar de subscribe()?

```typescript
// ANTES: 15 líneas, posible memory leak si olvidas unsubscribe
export class ProductsComponent implements OnInit, OnDestroy {
  products: Product[] = [];
  loading = false;
  private destroy$ = new Subject<void>();

  ngOnInit() {
    this.loading = true;
    this.productService.getAll()
      .pipe(takeUntil(this.destroy$))
      .subscribe(res => {
        this.products = res.data;
        this.loading = false;
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

// AHORA: 5 líneas, sin memory leaks, reactividad granular
export class ProductsComponent {
  productsResource = rxResource({ loader: () => this.productService.getAll() });
  products = computed(() => this.productsResource.value()?.data ?? []);
  loading = this.productsResource.isLoading;
  // No hay ngOnInit, no hay ngOnDestroy, no hay Subject
  // Angular limpia todo automáticamente
}
```

## ¿Por qué Flyway en lugar de hibernate.ddl-auto?

```
hibernate.ddl-auto=update:
  ✗ Puede borrar columnas si renombras un campo
  ✗ No puedes hacer rollback
  ✗ No sabes qué SQL ejecutó
  ✗ Diferente resultado en cada máquina
  ✗ NUNCA usar en producción

Flyway:
  ✓ SQL explícito y versionado (V1, V2, V3...)
  ✓ Mismo resultado en todas las máquinas
  ✓ Auditable en Git (quién cambió qué y cuándo)
  ✓ Rollback posible
  ✓ Estándar en producción enterprise
```
