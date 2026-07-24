# Cómo Ejecutar — Proyecto 10: Seguridad Full Stack

## Requisitos
- Backend del nivel 5 (api-auth con JWT) corriendo
- Node.js 22 + Angular CLI 20

## Paso 1: Backend (nivel 5)

```cmd
cd nivel-05-seguridad/proyecto/backend
mvn spring-boot:run
```

## Paso 2: Frontend

```cmd
cd nivel-10-seguridad-fullstack/proyecto/frontend
npm install
ng serve
```
→ http://localhost:4200

## Flujo
1. Abrir http://localhost:4200 → redirige a /login
2. Ingresar: admin / Admin123!
3. Se guarda el token en localStorage
4. Se redirige al dashboard
5. Cada petición HTTP lleva el token (interceptor)
6. Si el token expira → redirige a login

## Componentes principales

```
src/app/
├── pages/
│   ├── login/
│   ├── dashboard/
│   └── acceso-denegado/
├── services/
│   └── auth.service.ts        ← Login, logout, tokens, signals
├── interceptors/
│   └── auth.interceptor.ts    ← Agrega JWT a cada request
├── guards/
│   └── auth.guard.ts          ← Protege rutas
└── app.routes.ts              ← Rutas protegidas con canActivate
```
