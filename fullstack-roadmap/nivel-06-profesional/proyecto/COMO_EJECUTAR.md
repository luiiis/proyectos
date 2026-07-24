# Cómo Ejecutar — Proyecto 6: Funciones Profesionales

## Prerrequisitos
- Nivel 5 completado (usuarios + JWT funcionando)
- MySQL con tablas de seguridad
- (Opcional) SMTP de Gmail para correos reales

## Crear BD (si no existe)
```sql
CREATE DATABASE auth_db;
```

## Ejecutar
```cmd
cd nivel-06-profesional/proyecto/backend
mvn spring-boot:run
```

## Endpoints

### Perfil (requiere JWT)
```bash
# Ver perfil
curl http://localhost:8080/api/usuarios/perfil \
  -H "Authorization: Bearer TU_TOKEN"

# Actualizar perfil
curl -X PUT http://localhost:8080/api/usuarios/perfil \
  -H "Authorization: Bearer TU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Nuevo Nombre","email":"nuevo@mail.com"}'

# Cambiar contraseña
curl -X POST http://localhost:8080/api/usuarios/cambiar-password \
  -H "Authorization: Bearer TU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"passwordActual":"Admin123!","passwordNuevo":"NuevaPass123!"}'
```

### Recuperación de contraseña (público)
```bash
# Solicitar recuperación (envía email con token)
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@softwarelee.com"}'

# Restablecer con token (del email)
curl -X POST http://localhost:8080/api/auth/reset-password \
  -H "Content-Type: application/json" \
  -d '{"token":"uuid-del-correo","nuevoPassword":"NuevaPass123!"}'
```

## Configuración SMTP (Gmail)
En `application.yml` o variables de entorno:
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: tu-correo@gmail.com
    password: tu-app-password
```

Para obtener App Password:
1. myaccount.google.com → Seguridad
2. Verificación en 2 pasos → ACTIVAR
3. App Passwords → Generar → Copiar los 16 caracteres

## Estructura
```
src/main/java/com/softwarelee/profesional/
├── Application.java
├── controller/
│   ├── PerfilController.java
│   └── PasswordResetController.java
├── service/
│   ├── EmailService.java
│   ├── PasswordResetService.java
│   └── AuditoriaService.java
├── mapper/
│   ├── UsuarioMapper.java
│   ├── PasswordResetMapper.java
│   └── AuditoriaMapper.java
├── model/
│   ├── Usuario.java
│   ├── PasswordResetToken.java
│   └── RegistroAuditoria.java
└── scheduler/
    └── TokenCleanupScheduler.java
```
