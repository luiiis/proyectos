# Módulo 30: Proyecto Final - Sistema Empresarial

## 1. Descripción

Sistema de gestión empresarial completo con Angular + PrimeNG + RxJS.
Aplica todos los conceptos aprendidos: clean architecture, state management, testing, lazy loading.

## 2. Módulos del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                    SISTEMA EMPRESARIAL                       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  🔐 Auth          │  Login, Register, Forgot Password       │
│  📊 Dashboard     │  KPIs, Charts, Resumen                  │
│  👥 Usuarios      │  CRUD, Roles, Permisos                  │
│  🛡️ Roles         │  Gestión de roles y permisos            │
│  📦 Productos     │  CRUD, Categorías, Imágenes             │
│  🏭 Inventario    │  Stock, Movimientos, Alertas            │
│  💰 Ventas        │  POS, Pedidos, Facturación              │
│  📈 Reportes      │  Ventas, Inventario, Usuarios           │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## 3. Arquitectura del Proyecto

```
src/app/
├── core/
│   ├── services/          (AuthService, TokenService)
│   ├── guards/            (AuthGuard, RoleGuard)
│   ├── interceptors/      (AuthInterceptor, ErrorInterceptor)
│   ├── models/            (User, ApiResponse, PaginatedResponse)
│   └── constants/         (API_URL, ROLES)
├── shared/
│   ├── components/        (DataTable, Modal, ConfirmDialog, Spinner)
│   ├── directives/        (HasRole, Highlight)
│   ├── pipes/             (Currency, DateFormat)
│   └── utils/             (validators, helpers)
├── features/
│   ├── auth/
│   │   ├── pages/         (LoginPage, RegisterPage)
│   │   ├── services/      (AuthService)
│   │   └── auth.routes.ts
│   ├── dashboard/
│   │   ├── pages/         (DashboardPage)
│   │   ├── components/    (KpiCard, SalesChart)
│   │   └── dashboard.routes.ts
│   ├── usuarios/
│   │   ├── pages/         (UsuarioListPage, UsuarioDetailPage)
│   │   ├── components/    (UsuarioTable, UsuarioForm)
│   │   ├── services/      (UsuarioService)
│   │   └── usuario.routes.ts
│   ├── productos/
│   ├── inventario/
│   ├── ventas/
│   └── reportes/
├── app.component.ts
├── app.config.ts
└── app.routes.ts
```

## 4. Requisitos Técnicos

```typescript
// Checklist de implementación:
const requisitos = {
  arquitectura: [
    'Clean Architecture (core/shared/features)',
    'Smart/Dumb components',
    'Barrel exports + path aliases',
    'Feature-based structure',
  ],
  angular: [
    'Standalone components',
    'Signals para estado',
    'Nuevo control flow (@if, @for, @switch)',
    'Lazy loading por feature',
    '@defer para componentes pesados',
    'OnPush change detection',
  ],
  ui: [
    'PrimeNG DataTable con paginación/filtros',
    'Formularios reactivos con validación',
    'Dialogs para CRUD',
    'Responsive design',
    'Accesibilidad (ARIA, keyboard nav)',
  ],
  backend: [
    'HttpClient con interceptors',
    'Auth JWT (login/refresh)',
    'Error handling centralizado',
    'Loading states',
  ],
  testing: [
    'Unit tests para servicios',
    'Component tests con TestBed',
    'Coverage > 70%',
  ],
  devops: [
    'Docker multi-stage build',
    'GitHub Actions CI/CD',
    'Environment configs (dev/prod)',
  ],
};
```

## 5. Fases de Desarrollo

```
Fase 1 (Semana 1-2): Setup + Auth + Layout
├── Crear proyecto con CLI
├── Configurar arquitectura de carpetas
├── Implementar Auth (login, guards, interceptors)
└── Layout principal (toolbar, sidebar, router-outlet)

Fase 2 (Semana 3-4): CRUD Usuarios + Productos
├── Servicio genérico CRUD
├── DataTable reutilizable
├── Formularios con validación
└── Dialogs para crear/editar

Fase 3 (Semana 5-6): Inventario + Ventas
├── Gestión de stock
├── Punto de venta (POS)
├── Pedidos y facturación
└── State management con signals

Fase 4 (Semana 7-8): Reportes + Polish
├── Dashboard con gráficos
├── Reportes exportables
├── Testing (>70% coverage)
├── Docker + CI/CD
└── Performance optimization
```

## 6. Ejercicios (Entregables)

1. Implementa el módulo de Auth completo (login, register, guards, token refresh).
2. Crea un CRUD genérico reutilizable (service + table + form) y aplícalo a Usuarios y Productos.
3. Implementa el Dashboard con KPIs reales consumidos de la API.
4. Crea el módulo de Ventas con carrito, checkout y generación de factura.
5. Dockeriza la aplicación y configura CI/CD con GitHub Actions.

---

## Módulos Complementarios
→ [Ejercicios](../ejercicios/README.md)
→ [Entrevistas](../entrevistas/README.md)
→ [Evaluaciones](../evaluaciones/README.md)
