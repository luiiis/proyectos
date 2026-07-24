# Cómo Ejecutar — Nivel 10: Seguridad Full Stack (Login Angular + Spring Boot)

## Prerrequisitos
- Backend del Nivel 5 corriendo en :8080 (con JWT)
- Node.js 22 + Angular CLI 20

## Levantar backend
```cmd
cd nivel-05-seguridad/proyecto/backend
mvn spring-boot:run
# → http://localhost:8080 (con JWT activado)
```

## Levantar frontend
```cmd
cd nivel-10-seguridad-fullstack/proyecto/frontend
npm install
ng serve
# → http://localhost:4200
```

## Flujo completo
```
1. Abrir http://localhost:4200
2. Guard detecta que no hay token → redirige a /login
3. Escribir: admin / Admin123!
4. Angular envía POST /api/auth/login
5. Backend valida → devuelve JWT
6. Angular guarda token en localStorage
7. Redirige a /dashboard
8. Interceptor agrega "Authorization: Bearer <token>" a cada request
9. Si el token expira → redirige a /login automáticamente
```

## Archivos clave del frontend
```
src/app/
├── core/
│   ├── services/auth.service.ts       ← login(), logout(), getToken(), isAuthenticated()
│   ├── interceptors/auth.interceptor.ts ← Agrega JWT a cada HTTP request
│   └── guards/auth.guard.ts           ← Protege rutas (redirige si no hay token)
├── features/
│   ├── login/login.component.ts       ← Formulario de login
│   └── dashboard/dashboard.component.ts ← Página protegida
└── app.routes.ts                      ← canActivate: [authGuard]
```

## Probar
- Login correcto → ver dashboard
- Login incorrecto → ver error "Credenciales inválidas"
- Cerrar sesión → token se borra, redirige a login
- Navegar a /dashboard sin login → redirige a /login
