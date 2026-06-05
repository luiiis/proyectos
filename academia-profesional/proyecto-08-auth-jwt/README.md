# Proyecto 08: Autenticación JWT

## ¿Qué construimos?
API REST con Spring Security + JWT. Login, registro, protección de endpoints por roles.

## Tecnologías
- Spring Boot 3.3 + Spring Security 6
- JWT (jjwt 0.12.5)
- PostgreSQL (usuarios + roles)
- BCrypt (hash de passwords)

## Endpoints
```
POST /api/auth/register  → Crear cuenta (público)
POST /api/auth/login     → Obtener token (público)
GET  /api/auth/me        → Ver mi perfil (requiere token)
```

## Cómo funciona
```
1. POST /login {username, password}
2. Spring valida credenciales contra BD (BCrypt)
3. Si OK → genera JWT firmado con secret
4. Responde: {token: "eyJ...", username, rol}
5. Cliente guarda token
6. Cada request incluye: Authorization: Bearer eyJ...
7. JwtFilter valida token en cada petición
8. Si válido → acceso permitido. Si no → 401.
```

## Ejecutar
```bash
docker compose up -d          # PostgreSQL
mvn spring-boot:run           # API en :8080
curl -X POST localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"username":"admin","password":"admin123"}'
```
