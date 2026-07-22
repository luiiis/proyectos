# Cómo Ejecutar — Fullstack 2026

## Requisitos
- Docker Desktop 24+ con 6GB+ RAM asignados
- (Opcional para dev local) Java 21+, Maven 3.9+, Node 22+, Angular CLI 21

---

## Opción 1: Todo con Docker (más rápido)

```bash
cd fullstack-2026
cp .env.example .env
docker compose up --build -d

# Esperar ~60 segundos (Keycloak tarda)
# Verificar: docker compose ps (todos "running")
```

### Acceder:
| Servicio | URL |
|----------|-----|
| Frontend | http://localhost |
| Backend API | http://localhost:8080/api/products |
| Swagger | http://localhost:8080/swagger-ui.html |
| Keycloak Admin | http://localhost:8180 (admin/admin123) |
| Health Check | http://localhost:8080/actuator/health |

---

## Opción 2: Desarrollo local (BDs en Docker, código local)

```bash
# 1. Levantar infraestructura
docker compose up postgres redis keycloak -d

# 2. Configurar Keycloak (solo la primera vez)
# → Ver docs/GUIA-LEVANTAR-PROYECTO-2026.md paso 2

# 3. Backend
cd backend
mvn spring-boot:run
# → http://localhost:8080

# 4. Frontend (otra terminal)
cd frontend
npm install
ng serve
# → http://localhost:4200
```

---

## Probar la API

```bash
# 1. Obtener token de Keycloak
curl -X POST http://localhost:8180/realms/fullstack/protocol/openid-connect/token \
  -d "grant_type=password&client_id=frontend-app&username=admin&password=admin123"

# 2. Usar token (copiar access_token del paso anterior)
curl http://localhost:8080/api/products \
  -H "Authorization: Bearer TU_TOKEN"
```

---

## Detener

```bash
docker compose down        # Mantiene datos
docker compose down -v     # Borra datos (reset completo)
```

---

## Documentación completa
→ `docs/GUIA-LEVANTAR-PROYECTO-2026.md` (paso a paso con explicaciones)
→ `MANUAL-TECNICO.md` (arquitectura y decisiones)
