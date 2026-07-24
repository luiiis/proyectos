# Cómo Ejecutar — Proyecto 13: Microsoft Entra ID

## Requisitos
- Cuenta de Azure (puede ser gratuita)
- Registro de aplicación en Entra ID
- Java 21 + Maven
- Node.js 22 + Angular CLI 20

## Paso 1: Registrar app en Azure

1. Ir a: https://portal.azure.com → Entra ID → App registrations
2. New registration:
   - Name: "mi-app-fullstack"
   - Redirect URI: http://localhost:4200
3. Anotar: **Tenant ID** y **Client ID**
4. En "API permissions" → Add: User.Read
5. En "App roles" → Crear roles: Admin, User, Viewer
6. En "Expose an API" → Add a scope: api://tu-client-id/access

## Paso 2: Variables de entorno

```cmd
set AZURE_TENANT_ID=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
set AZURE_CLIENT_ID=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
set AZURE_CLIENT_SECRET=tu-secret
```

## Paso 3: Backend (Resource Server)

```cmd
cd nivel-13-entra-id/proyecto/backend
mvn spring-boot:run
```

El backend NO hace login, solo VALIDA tokens de Entra ID.

## Paso 4: Frontend (MSAL)

```cmd
cd nivel-13-entra-id/proyecto/frontend
npm install
ng serve
```

El frontend usa @azure/msal-angular para:
1. Redirigir al login de Microsoft
2. Obtener el token de Entra ID
3. Enviar el token al backend en cada request

## Flujo completo

```
1. Usuario → click "Login con Microsoft"
2. Angular → redirige a login.microsoftonline.com
3. Microsoft → usuario se autentica
4. Microsoft → redirige a Angular con un código
5. Angular (MSAL) → intercambia código por token
6. Angular → envía token al backend
7. Backend → valida token con clave pública de Microsoft
8. Backend → extrae roles → autoriza la operación
```

## Diferencia con JWT local

| Aspecto | JWT Local (nivel 5) | Entra ID (nivel 13) |
|---------|---------------------|---------------------|
| Quién genera el token | Tu backend | Microsoft |
| Quién valida el token | Tu backend (firma propia) | Tu backend (firma de Microsoft) |
| Gestión de usuarios | En tu BD | En Entra ID (Azure) |
| Login | Tu pantalla de login | Pantalla de Microsoft |
| MFA | Tú lo implementas | Microsoft lo gestiona |
| SSO | No | Sí (Office 365, Teams, etc.) |
