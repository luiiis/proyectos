# Nivel 10: Seguridad Full Stack — Proyecto 10: Login Angular + Spring Boot

## Objetivo
Implementar autenticación completa: login en Angular que se comunica con Spring Security + JWT.

## Funcionalidades
- Pantalla de login
- Pantalla de registro
- Guardar sesión (token en localStorage)
- Cerrar sesión
- Proteger rutas (Guard)
- Ocultar opciones según rol
- Renovar token (refresh)
- Mostrar perfil del usuario
- Cambiar contraseña
- Sesión expirada → redirigir a login

## Angular
- AuthService (login, logout, getToken, isAuthenticated)
- HTTP Interceptor (agrega JWT a cada request)
- Route Guard (protege rutas privadas)
- Roles y permisos en template (@if hasRole)
- Pantalla de "Acceso Denegado"

## Backend
- Spring Security configurado
- Endpoint /auth/login → devuelve JWT
- Endpoint /auth/register → crear usuario
- Endpoint /auth/refresh → renovar token
- Endpoint /auth/me → datos del usuario actual
- CORS configurado para Angular

## Documentación
- Flujo completo de autenticación (diagrama de secuencia)
- Matriz de permisos (quién puede ver qué)
- Manual de inicio de sesión
- Pruebas de seguridad
