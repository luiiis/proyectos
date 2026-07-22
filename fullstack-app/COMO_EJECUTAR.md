# Cómo Ejecutar — Fullstack App (Multi-BD)

## Requisitos
- Docker Desktop 24+ con **8GB+ RAM** (Oracle + SQL Server son pesados)
- (Opcional) Java 17+, Maven 3.9+, Node 20+, Angular CLI 17

---

## Opción 1: Todo con Docker

```bash
cd fullstack-app
cp .env.example .env

# Levantar todo (primera vez: ~5 min por descarga de Oracle/MSSQL)
docker compose up --build -d

# Verificar que todos arranquen (Oracle tarda ~90 segundos)
docker compose ps
docker compose logs -f oracle   # Esperar "DATABASE IS READY TO USE"
```

### Acceder:
| Servicio | URL | Credenciales |
|----------|-----|--------------|
| Frontend | http://localhost | — |
| Backend Auth API | http://localhost:8080/swagger-ui.html | JWT |
| Backend Mail API | http://localhost:8081 | JWT |
| MySQL | localhost:3306 | root / rootPassword123! |
| Oracle | localhost:1521 (SID: XEPDB1) | products_user / productsPass123! |
| SQL Server | localhost:1433 | sa / SqlServer123! |

### Login:
| Usuario | Password | Rol |
|---------|----------|-----|
| admin | Admin123! | ADMIN |
| manager | Manager123! | MANAGER |
| user1 | User123! | USER |

---

## Opción 2: Desarrollo local

```bash
# 1. Solo bases de datos
docker compose up mysql oracle sqlserver -d

# 2. Backend Auth (terminal 1)
cd backend-auth
mvn spring-boot:run
# → http://localhost:8080

# 3. Backend Mail (terminal 2)
cd backend-mail
mvn spring-boot:run
# → http://localhost:8081

# 4. Frontend (terminal 3)
cd frontend
npm install
ng serve
# → http://localhost:4200
```

---

## Probar la API

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"Admin123!"}'
# Copiar el token de la respuesta

# Listar productos (Oracle)
curl http://localhost:8080/api/products \
  -H "Authorization: Bearer TU_TOKEN"

# Listar usuarios (MySQL)
curl http://localhost:8080/api/users \
  -H "Authorization: Bearer TU_TOKEN"
```

---

## Detener

```bash
docker compose down        # Mantiene datos
docker compose down -v     # Borra datos (reset)
```

---

## Troubleshooting

| Error | Solución |
|-------|----------|
| Oracle tarda mucho | Normal. Esperar 90-120s. Ver logs: `docker compose logs oracle` |
| SQL Server "not ready" | Requiere 4GB+ RAM en Docker. Ajustar en Docker Desktop → Settings |
| "Authentication failed" | Verificar que los init-scripts crearon usuarios. `docker compose down -v && up` |
| Puerto en uso | Cambiar puertos en docker-compose.yml: `"3307:3306"` |

---

## Documentación adicional
→ `docs/09-GUIA-LEVANTAR-PROYECTO.md` (setup detallado)
→ `docs/DOCUMENTACION-COMPLETA.md` (manual extenso)
→ `docs/08-FLUJO-URL-A-PANTALLA.md` (cómo funciona un request)
→ `MANUAL-TECNICO.md` (arquitectura)
