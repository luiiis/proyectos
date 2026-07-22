# Ejercicios y Retos - Proyecto 08: Autenticación JWT

## Reto 1: Refresh Token
Implementa un sistema de refresh token:
- El login devuelve 2 tokens: access_token (15 min) + refresh_token (7 días)
- Endpoint `POST /api/auth/refresh` recibe el refresh_token y devuelve un nuevo access_token
- Si el refresh_token expira, el usuario debe hacer login de nuevo

**Lo que practicas:** JWT con múltiples tokens, expiración, flujo de renovación

---

## Reto 2: Roles y permisos
Implementa un sistema de roles:
- ROLE_ADMIN: puede hacer todo (CRUD completo)
- ROLE_VENDEDOR: puede crear ventas y ver productos
- ROLE_CONSULTOR: solo lectura (GET)

Proteger endpoints:
```java
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/{id}")
public void eliminar(@PathVariable Long id) { ... }
```

**Lo que practicas:** @PreAuthorize, roles, autorización granular

---

## Reto 3: Logout / Invalidar token
JWT es stateless (no se puede "invalidar" directamente). Implementa una solución:
- Opción A: Guardar tokens invalidados en Redis (blacklist) con TTL = expiración del token
- Opción B: Guardar en BD una lista de tokens revocados
- El JwtFilter debe verificar que el token NO esté en la blacklist

**Lo que practicas:** Invalidación de tokens, Redis/BD como cache, trade-offs

---

## Reto 4: Rate limiting en login
Protege contra ataques de fuerza bruta:
- Máximo 5 intentos de login fallidos por usuario en 15 minutos
- Después del 5to intento: bloquear cuenta por 30 minutos
- Devolver `429 Too Many Requests` con header `Retry-After`

**Lo que practicas:** Seguridad, rate limiting, protección contra ataques

---

## Reto 5: Password policies
Implementa validaciones de password:
- Mínimo 8 caracteres
- Al menos 1 mayúscula, 1 minúscula, 1 número, 1 carácter especial
- No puede ser igual al username
- No puede repetir las últimas 3 passwords usadas

**Lo que practicas:** Validación custom, histórico de passwords, seguridad

---

## Reto 6: Endpoint /me con datos completos
Extiende `GET /api/auth/me` para devolver:
```json
{
  "username": "admin",
  "email": "admin@mail.com",
  "roles": ["ROLE_ADMIN"],
  "ultimoLogin": "2026-07-21T10:30:00",
  "cuentaCreada": "2026-01-15",
  "intentosFallidos": 0
}
```

**Lo que practicas:** SecurityContext, datos del usuario autenticado

---

## Reto 7: OAuth2 con Google (avanzado)
Agrega login con Google:
1. Configurar credenciales en Google Cloud Console
2. Agregar `spring-boot-starter-oauth2-client`
3. El usuario puede hacer login con email/password O con Google
4. Si el email de Google ya existe en tu BD, vincular cuentas

**Lo que practicas:** OAuth2, proveedores externos, vinculación de cuentas

---

## Reto 8: Auditoría de accesos
Registra en BD cada acceso:
- Quién se logueó, cuándo, desde qué IP
- Intentos fallidos (username, IP, fecha)
- Endpoint `GET /api/auth/audit` (solo ADMIN) muestra los últimos 50 accesos

**Lo que practicas:** Auditoría, logging, seguridad, HttpServletRequest
