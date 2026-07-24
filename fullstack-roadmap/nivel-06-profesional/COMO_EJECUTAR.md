# Cómo Ejecutar — Nivel 6: Funciones Profesionales

## Prerrequisitos
- Nivel 5 completado (usuarios + JWT funcionando)
- MySQL con tablas de seguridad + SMTP (Gmail para desarrollo)

## Ejecutar
```cmd
cd nivel-06-profesional/proyecto/backend
mvn spring-boot:run
```

## Endpoints
```bash
# Perfil del usuario autenticado
GET /api/usuarios/perfil (con JWT)

# Cambiar contraseña
POST /api/usuarios/cambiar-password
Body: {"passwordActual": "xxx", "passwordNuevo": "yyy"}

# Solicitar recuperación (envía email con token)
POST /api/auth/forgot-password
Body: {"email": "usuario@mail.com"}

# Restablecer con token (del email)
POST /api/auth/reset-password
Body: {"token": "abc123", "nuevoPassword": "NuevaPass123!"}
```

## Configuración SMTP (Gmail)
En `application.yml`:
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: tu-correo@gmail.com
    password: tu-app-password    # NO la contraseña normal, es App Password
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
```

Para obtener App Password de Gmail:
1. Ir a myaccount.google.com → Seguridad
2. Verificación en 2 pasos → ACTIVAR
3. App Passwords → Generar → Copiar los 16 caracteres
