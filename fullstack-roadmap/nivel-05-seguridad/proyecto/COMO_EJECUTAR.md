# Cómo Ejecutar — Proyecto 5: Seguridad con JWT

## Requisitos
- Java 21 + Maven + MySQL corriendo

## Crear BD
```sql
CREATE DATABASE auth_db;
```

## Ejecutar
```cmd
cd nivel-05-seguridad/proyecto/backend
mvn spring-boot:run
```

## Probar

### Login (obtener token)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"Admin123!"}'
```
Respuesta: `{"accessToken": "eyJ...", "refreshToken": "eyJ...", "roles": ["ROLE_ADMIN"]}`

### Usar token (copiar el accessToken)
```bash
curl http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer TU_ACCESS_TOKEN"
```

### Sin token (401)
```bash
curl http://localhost:8080/api/auth/me
# Respuesta: 401 Unauthorized
```

### Refresh token
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"TU_REFRESH_TOKEN"}'
```

## Usuarios de prueba

| Usuario | Password | Rol |
|---------|----------|-----|
| admin | Admin123! | ADMIN |
| supervisor | Super123! | SUPERVISOR |
| cajero | Cajero123! | CAJERO |

## Flujo de autenticación
```
1. POST /login → recibe token
2. Guardar token en el cliente
3. En cada request: Header "Authorization: Bearer <token>"
4. El JwtFilter valida el token
5. Si es válido → acceso permitido
6. Si no → 401 Unauthorized
```
