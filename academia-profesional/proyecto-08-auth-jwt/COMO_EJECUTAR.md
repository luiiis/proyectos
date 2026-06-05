# Cómo Ejecutar - Proyecto 08: Auth JWT

## 1. Levantar PostgreSQL
```bash
cd academia-profesional/proyecto-08-auth-jwt
docker compose up -d
```

## 2. Ejecutar la API
```bash
mvn spring-boot:run
```

## 3. Probar

### Registrar usuario
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123","email":"admin@mail.com","nombre":"Admin"}'
```

### Login (obtener token)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
# Respuesta: {"token":"eyJ...","username":"admin","rol":"USER","expiresIn":86400000}
```

### Usar token en endpoints protegidos
```bash
TOKEN="eyJ..."  # Pegar el token del login
curl http://localhost:8080/api/auth/me -H "Authorization: Bearer $TOKEN"
```

### Sin token → 401
```bash
curl http://localhost:8080/api/auth/me
# Respuesta: 401 Unauthorized
```
