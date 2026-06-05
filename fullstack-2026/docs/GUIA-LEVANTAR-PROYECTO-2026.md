# Guía Completa: Levantar el Proyecto 2026 desde Cero

## Prerrequisitos

```
1. Docker Desktop (incluye Docker Compose)
   - Windows/Mac: https://www.docker.com/products/docker-desktop/
   - Asignar mínimo 6GB RAM en Settings → Resources
   - Verificar: docker --version → 24+
   - Verificar: docker compose version → v2+

2. (Opcional para desarrollo local sin Docker)
   - Java 25: https://adoptium.net/ (Temurin JDK 25)
   - Maven 3.9+: https://maven.apache.org/
   - Node.js 22: https://nodejs.org/
   - Angular CLI: npm install -g @angular/cli@21
```

---

## Paso 1: Levantar SOLO las Bases de Datos con Docker

Este es el flujo recomendado para desarrollo: BDs en Docker, código local.

```bash
cd fullstack-2026

# Crear archivo .env
cp .env.example .env

# Levantar PostgreSQL + Redis + Keycloak
docker compose up postgres redis keycloak -d

# Verificar que están corriendo
docker compose ps

# Esperar ~30 segundos a que Keycloak arranque
# Verificar PostgreSQL:
docker exec -it fs2026-postgres psql -U postgres -d fullstack2026 -c "SELECT 1;"

# Verificar Redis:
docker exec -it fs2026-redis redis-cli ping
# Respuesta: PONG
```

### ¿Qué ocurre al ejecutar `docker compose up postgres redis keycloak -d`?

```
Segundo 0:  Docker lee docker-compose.yml
Segundo 1:  Crea red "app-net" (red virtual interna)
Segundo 2:  Crea volúmenes "postgres_data" y "redis_data"
Segundo 3:  Descarga imágenes (primera vez: ~500MB total)
Segundo 5:  Inicia PostgreSQL
            - Crea BD "fullstack2026" automáticamente (POSTGRES_DB)
            - Crea usuario "postgres" con password del .env
Segundo 5:  Inicia Redis
            - Listo en ~1 segundo
            - Límite 256MB RAM, política LRU (borra lo menos usado)
Segundo 10: PostgreSQL pasa healthcheck (pg_isready)
Segundo 12: Inicia Keycloak
            - Se conecta a PostgreSQL (usa la misma BD, schema separado)
            - Crea sus tablas internas automáticamente
            - Crea usuario admin/admin123
Segundo 30: Keycloak listo en http://localhost:8180
```

---

## Paso 2: Configurar Keycloak (Identity Provider)

Keycloak maneja TODO lo relacionado con autenticación. Necesitas crear un "Realm" y un "Client".

### 2.1 Acceder a Keycloak Admin Console

```
URL: http://localhost:8180
Usuario: admin
Password: admin123 (o lo que pusiste en .env)
```

### 2.2 Crear Realm "fullstack"

```
1. Clic en el dropdown "master" (arriba a la izquierda)
2. Clic en "Create realm"
3. Realm name: fullstack
4. Clic "Create"
```

### 2.3 Crear Client "frontend-app"

```
1. Menú izquierdo → Clients → Create client
2. Client ID: frontend-app
3. Client type: OpenID Connect
4. Clic "Next"
5. Client authentication: OFF (es una SPA pública)
6. Authorization: OFF
7. Clic "Next"
8. Valid redirect URIs: http://localhost:4200/*
9. Valid post logout redirect URIs: http://localhost:4200
10. Web origins: http://localhost:4200
11. Clic "Save"
```

### 2.4 Crear Roles

```
1. Menú izquierdo → Realm roles → Create role
2. Crear 3 roles:
   - ADMIN (descripción: Administrador del sistema)
   - MANAGER (descripción: Gerente de productos)
   - USER (descripción: Usuario estándar)
```

### 2.5 Crear Usuarios de Prueba

```
1. Menú izquierdo → Users → Add user
2. Crear usuario "admin":
   - Username: admin
   - Email: admin@sistema.com
   - First name: Admin
   - Last name: Sistema
   - Email verified: ON
   - Clic "Create"
3. Tab "Credentials":
   - Set password: admin123
   - Temporary: OFF
   - Clic "Save"
4. Tab "Role mapping":
   - Assign role → Filter by realm roles
   - Seleccionar: ADMIN, USER
   - Clic "Assign"

5. Repetir para "manager1":
   - Username: manager1, password: manager123
   - Roles: MANAGER, USER

6. Repetir para "user1":
   - Username: user1, password: user123
   - Roles: USER
```

### 2.6 Verificar que funciona

```bash
# Obtener token de Keycloak (simula lo que hace el frontend)
curl -X POST http://localhost:8180/realms/fullstack/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=frontend-app" \
  -d "username=admin" \
  -d "password=admin123"

# Debes recibir un JSON con access_token, refresh_token, etc.
# Copia el access_token para probar el backend
```

---

## Paso 3: Levantar el Backend (Desarrollo Local)

```bash
cd fullstack-2026/backend

# Configurar variables de entorno (PowerShell en Windows)
$env:DB_HOST="localhost"
$env:DB_PORT="5432"
$env:DB_NAME="fullstack2026"
$env:DB_USER="postgres"
$env:DB_PASSWORD="postgres123"
$env:REDIS_HOST="localhost"
$env:REDIS_PORT="6379"
$env:KEYCLOAK_URL="http://localhost:8180"
$env:KEYCLOAK_REALM="fullstack"

# Compilar y ejecutar
mvn spring-boot:run

# Debes ver:
# "Started Application in X seconds (process running with PID ...)"
# "Flyway: Successfully applied 2 migrations"
```

### ¿Qué ocurre al ejecutar `mvn spring-boot:run`?

```
1. Maven lee pom.xml → verifica dependencias
2. Compila código Java → target/classes/
3. Spring Boot arranca:
   a. Lee application.yml
   b. Habilita Virtual Threads (spring.threads.virtual.enabled=true)
   c. Conecta a PostgreSQL (HikariCP pool: 5-20 conexiones)
   d. Flyway ejecuta migraciones:
      - V1__initial_schema.sql → Crea tablas
      - V2__seed_data.sql → Inserta datos de prueba
   e. Conecta a Redis (caché)
   f. Descarga claves públicas de Keycloak (para validar JWT)
   g. Registra endpoints REST
   h. Inicia Tomcat en puerto 8080
4. Listo para recibir peticiones
```

### Verificar que funciona:

```bash
# Sin autenticación (debe dar 401)
curl http://localhost:8080/api/products
# Respuesta: 401 Unauthorized

# Con token de Keycloak (obtener token del paso 2.6)
curl http://localhost:8080/api/products \
  -H "Authorization: Bearer TU_ACCESS_TOKEN_AQUI"
# Respuesta: JSON con 15 productos

# Swagger UI (documentación interactiva)
# Abrir: http://localhost:8080/swagger-ui.html

# Health check
curl http://localhost:8080/actuator/health
# Respuesta: {"status":"UP"}

# Métricas Prometheus
curl http://localhost:8080/actuator/prometheus
# Respuesta: métricas en formato Prometheus
```

---

## Paso 4: Verificar Base de Datos

### Conectar a PostgreSQL

```bash
# Desde terminal (Docker)
docker exec -it fs2026-postgres psql -U postgres -d fullstack2026

# Dentro de psql:
\dt                          -- Listar todas las tablas
\d products                  -- Describir tabla products
SELECT * FROM products;      -- Ver productos
SELECT * FROM users;         -- Ver usuarios
SELECT * FROM roles;         -- Ver roles
SELECT * FROM flyway_schema_history;  -- Ver migraciones aplicadas
\q                           -- Salir
```

### Queries útiles:

```sql
-- Ver productos con stock bajo
SELECT name, stock, price FROM products WHERE stock < 15 ORDER BY stock;

-- Ver movimientos de inventario de un producto
SELECT im.type, im.quantity, im.reason, im.created_at
FROM inventory_movements im
WHERE im.product_id = 1
ORDER BY im.created_at DESC;

-- Ver audit logs (JSONB query)
SELECT action, username, details->>'name' as product_name, created_at
FROM audit_logs
WHERE entity_type = 'Product'
ORDER BY created_at DESC;

-- Full-text search en productos (PostgreSQL nativo)
SELECT name, price FROM products
WHERE to_tsvector('spanish', name) @@ to_tsquery('spanish', 'laptop');

-- Valor total del inventario por categoría
SELECT category, 
       COUNT(*) as total_products,
       SUM(price * stock) as inventory_value
FROM products
WHERE is_active = true
GROUP BY category
ORDER BY inventory_value DESC;
```

### Conectar con DBeaver/pgAdmin (GUI)

```
Host: localhost
Port: 5432
Database: fullstack2026
Username: postgres
Password: postgres123
```

---

## Paso 5: Levantar el Frontend (Desarrollo Local)

```bash
cd fullstack-2026/frontend

# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
ng serve

# Abrir: http://localhost:4200
# Angular se recarga automáticamente al cambiar código
```

### Flujo de autenticación en el frontend:

```
1. Usuario abre http://localhost:4200
2. Angular carga → AuthService.init() → Keycloak check-sso
3. No hay sesión → muestra botón "Iniciar Sesión"
4. Clic en "Iniciar Sesión" → AuthService.login()
5. Keycloak REDIRIGE a http://localhost:8180/realms/fullstack/protocol/openid-connect/auth
6. Usuario ve formulario de Keycloak (NO de Angular)
7. Escribe admin/admin123 → Keycloak valida
8. Keycloak REDIRIGE de vuelta a http://localhost:4200 con token en URL
9. Keycloak JS adapter extrae el token
10. AuthService actualiza signals: isAuthenticated=true, username="admin"
11. Angular muestra el dashboard
12. Cada petición HTTP incluye: Authorization: Bearer <token>
```

---

## Paso 6: Levantar TODO con Docker (Producción)

```bash
cd fullstack-2026

# Construir y levantar todo
docker compose up --build -d

# Ver logs
docker compose logs -f backend

# Verificar
docker compose ps
# Todos deben estar "running" y "healthy"

# Acceder:
# Frontend: http://localhost
# Backend API: http://localhost:8080
# Keycloak Admin: http://localhost:8180
# Swagger: http://localhost:8080/swagger-ui.html
```

---

## Paso 7: Verificar Redis (Caché)

```bash
# Conectar a Redis
docker exec -it fs2026-redis redis-cli

# Ver todas las claves
KEYS *

# Después de hacer GET /api/products, verás:
KEYS *
# "products::all"

# Ver contenido de una clave
GET "products::all"

# Ver TTL (tiempo restante antes de expirar)
TTL "products::all"
# Respuesta: ~300 (5 minutos en segundos)

# Borrar caché manualmente
DEL "products::all"

# Monitorear en tiempo real (ver cada operación)
MONITOR
# (Ctrl+C para salir)
```

---

## Paso 8: Ejecutar Tests

```bash
cd fullstack-2026/backend

# Ejecutar todos los tests (usa Testcontainers → levanta PostgreSQL real en Docker)
mvn test

# Ejecutar un test específico
mvn test -Dtest="ProductServiceTest"

# NOTA: Testcontainers necesita Docker corriendo
# Automáticamente:
# 1. Descarga imagen postgres:16-alpine
# 2. Levanta un contenedor temporal
# 3. Ejecuta migraciones Flyway
# 4. Corre los tests contra BD REAL
# 5. Destruye el contenedor al terminar
```

---

## Troubleshooting

### "Connection refused" al backend
```
Causa: Backend no ha terminado de arrancar o Keycloak no está listo
Solución: 
  docker compose logs backend  → ver errores
  Esperar 30-60 segundos después de levantar Keycloak
```

### "401 Unauthorized" en todas las peticiones
```
Causa: Token inválido o Keycloak no configurado
Solución:
  1. Verificar que el realm "fullstack" existe en Keycloak
  2. Verificar que el client "frontend-app" existe
  3. Obtener token fresco con curl (paso 2.6)
```

### Flyway falla con "migration checksum mismatch"
```
Causa: Modificaste un archivo V1 o V2 después de aplicarlo
Solución: NUNCA modificar migraciones ya aplicadas. Crear V3__fix.sql
  O en desarrollo: docker compose down -v (BORRA DATOS) y recrear
```

### "Port already in use"
```
Causa: Otro servicio usa el puerto
Solución:
  # Ver qué usa el puerto (Windows)
  netstat -ano | findstr :5432
  # Cambiar puerto en docker-compose.yml:
  ports:
    - "5433:5432"  # Usar 5433 externamente
```

### Redis no cachea (siempre consulta BD)
```
Causa: Redis no está corriendo o la config es incorrecta
Solución:
  docker exec -it fs2026-redis redis-cli ping  → debe decir PONG
  Verificar REDIS_HOST y REDIS_PORT en variables de entorno
```

---

## Resumen de Puertos

| Servicio | Puerto | URL | Credenciales |
|----------|--------|-----|--------------|
| Frontend | 80 (Docker) / 4200 (dev) | http://localhost | - |
| Backend | 8080 | http://localhost:8080 | Token JWT |
| Swagger | 8080 | http://localhost:8080/swagger-ui.html | - |
| PostgreSQL | 5432 | - | postgres / postgres123 |
| Redis | 6379 | - | sin password |
| Keycloak | 8180 | http://localhost:8180 | admin / admin123 |

---

## Comandos Docker Útiles

```bash
# Ver logs de un servicio
docker compose logs -f backend

# Reiniciar un servicio
docker compose restart backend

# Entrar a un contenedor
docker exec -it fs2026-postgres bash
docker exec -it fs2026-backend sh

# Ver uso de recursos
docker stats

# Parar todo (mantiene datos)
docker compose down

# Parar todo Y borrar datos (reset completo)
docker compose down -v

# Reconstruir después de cambios en código
docker compose up --build backend -d
```
