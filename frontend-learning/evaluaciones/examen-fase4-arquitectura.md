# Examen Fase 4: Arquitectura + DevOps Frontend (Módulos 22-30)

## Instrucciones
- Tiempo: 120 minutos
- Proyecto completo funcional

---

## Sección A: Teoría (30 puntos)

1. ¿Cuándo usarías Signals vs NgRx Signal Store vs NgRx Store?
2. Explica el patrón Smart/Dumb components con un ejemplo.
3. ¿Cómo implementarías accesibilidad (a11y) en un DataTable?
4. ¿Qué es Module Federation y cuándo justifica micro-frontends?
5. ¿Cómo configurarías CI/CD para un proyecto Angular? (build, test, deploy)
6. Explica las estrategias de Change Detection y cuándo usar OnPush.
7. ¿Cómo haces SEO en una SPA Angular? (SSR, pre-rendering)
8. ¿Cómo manejas feature flags en frontend?
9. ¿Cuál es tu estrategia de testing? (unit vs integration vs e2e)
10. ¿Cómo migrarías una app legacy (ExtJS/AngularJS) a Angular moderno?

---

## Sección B: Proyecto (70 puntos)

### Construir: Dashboard Empresarial

**B1. Arquitectura (15 pts)**
Implementa la estructura de carpetas recomendada:
- `core/`: auth service, interceptors, guards
- `shared/`: componentes genéricos reutilizables (DataTable, Card, Modal)
- `features/`: 3 módulos con lazy loading (dashboard, productos, ventas)
- Path aliases configurados en tsconfig

**B2. Componente Genérico DataTable (20 pts)**
Crea un componente `<app-data-table>` reutilizable:
```typescript
// Uso esperado:
<app-data-table
  [data]="productos()"
  [columns]="columnas"
  [paginator]="true"
  [pageSize]="10"
  [sortable]="true"
  [filterable]="true"
  (rowClick)="onRowClick($event)"
  (pageChange)="onPageChange($event)"
/>
```
Requisitos: genérico (funciona con cualquier tipo de dato), server-side pagination, sort, filter.

**B3. Estado con Signals (15 pts)**
Implementa un `ProductoStore` que:
- Mantenga lista de productos
- Soporte filtros (categoría, precio, búsqueda)
- Maneje loading/error states
- Paginación server-side
- Sea inyectable y testeable

**B4. Testing (10 pts)**
Escribe tests para:
- 3 unit tests del DataTable component
- 2 unit tests del ProductoStore
- 1 test de integración del flujo completo (service → component → render)

**B5. Docker + Performance (10 pts)**
- Dockerfile multi-stage (build Angular + serve con Nginx)
- Nginx config con caché, gzip, SPA routing
- Bundle analysis: identificar y eliminar imports innecesarios
- Implementar `@defer` en al menos un componente pesado

---

## Aprobación
- 50+ = Entiende arquitectura frontend
- 70+ = Puede liderar features complejas
- 85+ = Nivel senior frontend / puede definir arquitectura
