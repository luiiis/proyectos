# Cómo se Construyó - Proyecto 08: Auth JWT

## Paso 1: ¿Qué necesitamos?
- Registrar usuarios (con password hasheado)
- Login que devuelve un token JWT
- Filtro que valida el token en cada request
- Proteger endpoints según rol

## Paso 2: Componentes creados
```
entity/Usuario.java        → implements UserDetails (Spring Security lo requiere)
repository/UsuarioRepo     → findByUsername, existsByUsername
security/JwtService        → generate(), extractUsername(), isValid()
security/JwtFilter         → intercepta cada request, valida token
security/SecurityConfig    → reglas: qué es público, qué requiere auth
controller/AuthController  → /login, /register, /me
service/UserDetailsImpl    → carga usuario de BD para Spring Security
```

## Paso 3: Flujo de login
```
Request POST /api/auth/login {username:"admin", password:"admin123"}
  → AuthController.login()
  → authManager.authenticate() → Spring carga usuario de BD → BCrypt compara hash
  → Si OK: jwtService.generate(user) → token firmado con HMAC-SHA256
  → Responde: {token:"eyJ...", username:"admin", rol:"ROLE_ADMIN"}
```

## Paso 4: Flujo de request protegido
```
Request GET /api/auth/me + Header: Authorization: Bearer eyJ...
  → JwtFilter.doFilterInternal()
  → Extrae token del header
  → jwtService.extractUsername(token) → "admin"
  → userDetailsService.loadByUsername("admin") → carga de BD
  → jwtService.isValid(token, user) → verifica firma + expiración
  → Si válido: SecurityContext.setAuthentication(user)
  → Controller se ejecuta normalmente
```

## Paso 5: Probar
```bash
# 1. Levantar BD
docker compose up -d

# 2. Ejecutar
mvn spring-boot:run

# 3. Registrar
curl -X POST localhost:8080/api/auth/register -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123","email":"admin@mail.com"}'

# 4. Login
curl -X POST localhost:8080/api/auth/login -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
# Copiar el token de la respuesta

# 5. Usar token
curl localhost:8080/api/auth/me -H "Authorization: Bearer TU_TOKEN"

# 6. Sin token → 401
curl localhost:8080/api/auth/me
```
