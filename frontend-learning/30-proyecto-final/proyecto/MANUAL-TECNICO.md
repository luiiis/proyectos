# Módulo 30: Proyecto Final Frontend - Manual Técnico

## ¿Qué construimos?
Sistema empresarial completo en Angular que integra TODO lo aprendido en los 29 módulos.

## Stack
- Angular 17+ (Standalone Components, Signals)
- PrimeNG (DataTable, Dialog, Sidebar, TabView)
- RxJS (HttpClient, state management)
- Reactive Forms (validaciones complejas)
- Lazy Loading (cada módulo se carga bajo demanda)
- JWT Authentication (interceptor + guard)
- Docker (Nginx para producción)

## Arquitectura del Proyecto

```
src/app/
├── core/                          ← Singleton (1 instancia para toda la app)
│   ├── guards/
│   │   └── auth.guard.ts         ← Protege rutas que requieren login
│   ├── interceptors/
│   │   └── auth.interceptor.ts   ← Agrega JWT a cada petición HTTP
│   ├── services/
│   │   ├── auth.service.ts       ← Login, logout, token management
│   │   └── notification.service.ts
│   └── models/
│       ├── user.model.ts
│       └── api-response.model.ts
│
├── shared/                        ← Componentes reutilizables
│   ├── components/
│   │   ├── data-table/           ← Tabla genérica con filtro/sort/paginate
│   │   ├── confirm-dialog/       ← Diálogo de confirmación
│   │   └── loading-spinner/
│   ├── pipes/
│   │   └── currency-mx.pipe.ts  ← Formato moneda mexicana
│   └── directives/
│       └── has-role.directive.ts ← Mostrar/ocultar según rol
│
├── features/                      ← Módulos de negocio (lazy loaded)
│   ├── auth/
│   │   ├── login/
│   │   └── register/
│   ├── dashboard/
│   │   └── dashboard.component.ts ← KPIs, gráficas, alertas
│   ├── productos/
│   │   ├── lista/                ← DataTable con CRUD
│   │   ├── detalle/              ← Formulario crear/editar
│   │   └── producto.service.ts
│   ├── ventas/
│   │   ├── nueva-venta/          ← Stepper multi-paso
│   │   ├── historial/
│   │   └── venta.service.ts
│   ├── inventario/
│   │   ├── stock/
│   │   ├── movimientos/
│   │   └── alertas/
│   ├── clientes/
│   ├── usuarios/
│   └── reportes/
│       ├── ventas-periodo/
│       ├── top-productos/
│       └── rendimiento-vendedores/
│
├── app.component.ts               ← Shell (sidebar + toolbar + router-outlet)
├── app.config.ts                  ← Providers globales
└── app.routes.ts                  ← Rutas con lazy loading
```

## Cómo levantar

```bash
cd frontend-learning/30-proyecto-final/proyecto

# Instalar dependencias
npm install

# Desarrollo (con hot reload)
ng serve
# → http://localhost:4200

# Producción (Docker)
docker build -t proyecto-final-frontend .
docker run -p 80:80 proyecto-final-frontend
# → http://localhost
```

## Flujo: Login → Dashboard → Crear Venta

```
1. Usuario abre http://localhost:4200
2. AuthGuard verifica token → no hay → redirige a /login
3. Usuario escribe credenciales → POST /api/auth/login
4. Backend responde con JWT → se guarda en localStorage
5. Redirige a /dashboard
6. DashboardComponent carga KPIs (GET /api/reportes/dashboard)
7. Usuario navega a /ventas/nueva
8. Stepper: Paso 1 (cliente) → Paso 2 (productos) → Paso 3 (pago)
9. Confirmar → POST /api/ventas (con JWT en header via interceptor)
10. Backend registra venta → responde 201
11. Notificación toast: "Venta registrada exitosamente"
12. Redirige a /ventas/historial
```

## Criterios de completitud
- [ ] Login/Logout con JWT
- [ ] Dashboard con KPIs reales
- [ ] CRUD de productos (DataTable + Dialog)
- [ ] Registro de ventas (Stepper multi-paso)
- [ ] Inventario con alertas de stock bajo
- [ ] Reportes con filtros de fecha
- [ ] Lazy loading en todas las rutas
- [ ] Responsive (funciona en móvil)
- [ ] Interceptor JWT automático
- [ ] Guard de autenticación
- [ ] Manejo de errores global
- [ ] Al menos 20 tests unitarios
