# Proyecto 09: Dashboard Angular

## ¿Qué construimos?
SPA (Single Page Application) con Angular + Material que muestra un dashboard con KPIs y navegación protegida.

## Tecnologías
- Angular 17 (Standalone Components, Signals)
- Angular Material (toolbar, cards)
- JWT Interceptor (agrega token automáticamente)
- Auth Guard (protege rutas)
- Lazy Loading (carga bajo demanda)

## Pantallas
- `/login` → Formulario de autenticación
- `/dashboard` → KPIs (ventas, pedidos, satisfacción, alertas)

## Cómo funciona
```
1. Usuario abre /dashboard → AuthGuard verifica token
2. No hay token → redirige a /login
3. Usuario escribe credenciales → POST /api/auth/login
4. Backend responde con JWT → se guarda en localStorage
5. Interceptor agrega "Authorization: Bearer token" a cada petición
6. Redirige a /dashboard → muestra KPIs
```

## Ejecutar
```bash
# Necesitas el backend del proyecto 08 corriendo en :8080
cd academia-profesional/proyecto-09-dashboard-angular
# Si ya tienes un proyecto Angular creado:
ng serve
# → http://localhost:4200
```

## Estructura
```
src/app/
├── core/services/auth.service.ts      ← Login, logout, token (Signals)
├── core/guards/auth.guard.ts          ← Protege rutas
├── core/interceptors/auth.interceptor ← Agrega JWT a requests
├── features/login/                    ← Pantalla de login
├── features/dashboard/                ← Pantalla principal
└── app.routes.ts                      ← Rutas con lazy loading
```
