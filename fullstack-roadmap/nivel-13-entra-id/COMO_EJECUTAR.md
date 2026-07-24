# Cómo Ejecutar — Nivel 13: Microsoft Entra ID

## Prerrequisitos
- Cuenta de Azure (free tier funciona)
- El proyecto del Nivel 12 funcionando
- Registrar una aplicación en Azure Portal

## Configurar Azure (una sola vez)

### 1. Registrar aplicación en Azure
```
1. Ir a portal.azure.com → Microsoft Entra ID → App registrations
2. New registration:
   - Name: "Mi ERP Frontend"
   - Supported account types: Single tenant
   - Redirect URI: http://localhost:4200
3. Guardar: Application (client) ID y Directory (tenant) ID
```

### 2. Configurar el backend como Resource Server
En `application.yml`:
```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://login.microsoftonline.com/{TENANT_ID}/v2.0
```

### 3. Configurar Angular con MSAL
```bash
npm install @azure/msal-browser @azure/msal-angular
```

## Ejecutar
```cmd
# Backend (valida tokens de Entra ID)
cd backend && mvn spring-boot:run

# Frontend (login redirige a Microsoft)
cd frontend && ng serve
```

## Flujo de autenticación
```
1. Usuario abre http://localhost:4200
2. Angular detecta que no hay sesión → MSAL redirige a login.microsoft.com
3. Usuario escribe sus credenciales de Microsoft (o Active Directory corporativo)
4. Microsoft valida → redirige de vuelta con un token
5. Angular envía el token en cada request al backend
6. Backend valida la firma del token con las claves públicas de Microsoft
7. Si es válido → extrae roles y permisos → autoriza
```

## Diferencia con JWT custom (Nivel 5)
| JWT Custom | Microsoft Entra ID |
|-----------|-------------------|
| TÚ generas el token | MICROSOFT genera el token |
| TÚ manejas passwords | MICROSOFT maneja passwords |
| Sin MFA | MFA incluido gratis |
| Sin social login | Login con cuenta Microsoft/Google |
| Para apps internas | Para apps corporativas |
