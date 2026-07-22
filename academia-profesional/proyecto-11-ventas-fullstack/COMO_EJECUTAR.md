# Cómo Ejecutar - Proyecto 11: Sistema Ventas Full Stack

## Opción 1: Docker Compose (recomendado, todo junto)

```bash
cd academia-profesional/proyecto-11-ventas-fullstack
docker compose up --build -d

# Esperar ~30 segundos (build + arranque)

# Frontend: http://localhost
# API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
# Login: admin / admin123
```

## Opción 2: Desarrollo local (backend + frontend por separado)

### 1. Levantar PostgreSQL
```bash
cd academia-profesional/proyecto-11-ventas-fullstack
docker compose up postgres -d

# Verificar que la BD está lista:
docker exec -it ventas-fs-postgres psql -U postgres -d ventas_db -c "SELECT * FROM usuarios;"
```

### 2. Backend (Spring Boot)
```bash
cd academia-profesional/proyecto-11-ventas-fullstack/backend
mvn spring-boot:run
# API disponible en http://localhost:8080
```

### 3. Frontend (Angular + PrimeNG)
```bash
cd academia-profesional/proyecto-11-ventas-fullstack/frontend
npm install
ng serve
# Frontend en http://localhost:4200
```

## Probar la API con curl

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
# Copiar el token de la respuesta

# Listar productos (con token)
curl http://localhost:8080/api/productos \
  -H "Authorization: Bearer TU_TOKEN"

# Dashboard KPIs
curl http://localhost:8080/api/ventas/dashboard \
  -H "Authorization: Bearer TU_TOKEN"

# Registrar venta
curl -X POST http://localhost:8080/api/ventas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN" \
  -d '{"clienteId":1,"items":[{"productoId":1,"cantidad":2},{"productoId":3,"cantidad":1}]}'
```

## Credenciales de prueba

| Usuario | Password | Rol |
|---------|----------|-----|
| admin | admin123 | ADMIN |
| vendedor | password | VENDEDOR |

## Solución de errores

| Error | Causa | Solución |
|-------|-------|----------|
| Connection refused :5432 | PostgreSQL no arrancó | `docker compose up postgres -d` y esperar |
| 401 Unauthorized | Token inválido o expirado | Hacer login de nuevo |
| Port 8080 in use | Otro servicio en ese puerto | Cambiar puerto en application.yml |
| CORS error | Frontend en otro origen | Backend ya tiene CORS configurado para :4200 |

## Estructura del proyecto
```
proyecto-11-ventas-fullstack/
├── docker-compose.yml          ← Orquesta los 3 servicios
├── backend/
│   ├── Dockerfile              ← Multi-stage build
│   ├── pom.xml                 ← Dependencias Maven
│   ├── sql/01-schema.sql       ← Crea tablas + datos iniciales
│   └── src/main/java/com/academia/ventas/
│       ├── Application.java
│       ├── entity/             ← Entidades JPA (tablas)
│       ├── repository/         ← Acceso a datos
│       ├── controller/         ← Endpoints REST
│       └── security/           ← JWT + Spring Security
├── frontend/
│   ├── Dockerfile              ← Build Angular + nginx
│   ├── nginx.conf              ← Reverse proxy a API
│   ├── package.json
│   └── src/app/
│       ├── pages/              ← Componentes de cada pantalla
│       ├── services/           ← AuthService
│       ├── interceptors/       ← Agrega JWT a requests
│       └── guards/             ← Protege rutas
└── README.md
```
