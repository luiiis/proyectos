# Cómo Ejecutar - ERP Empresarial

## Prerrequisitos
```bash
docker --version        # Docker 24+
docker compose version  # Compose v2+
java -version           # Java 21+ (solo para desarrollo local)
node -version           # Node 20+ (solo para desarrollo local)
```

## Opción 1: Todo con Docker (producción)
```bash
cd academia-profesional/proyecto-final-erp
docker compose up --build -d

# Esperar 60 segundos (Kafka tarda)
docker compose ps  # Verificar todo "healthy"

# Acceder:
# Frontend:  http://localhost
# API:       http://localhost:8080/api
# Swagger:   http://localhost:8080/swagger-ui.html
# Grafana:   http://localhost:3000 (admin/admin)
# PgAdmin:   http://localhost:5050
```

## Opción 2: Desarrollo local
```bash
# 1. Infraestructura en Docker
docker compose up postgres redis kafka -d

# 2. Backend
cd backend
mvn spring-boot:run

# 3. Frontend (otra terminal)
cd frontend
npm install
ng serve

# Acceder: http://localhost:4200
```

## Credenciales de prueba
| Usuario | Password | Rol |
|---------|----------|-----|
| admin | admin123 | ADMIN |
| gerente | gerente123 | GERENTE |
| vendedor | vendedor123 | VENDEDOR |

## Resetear datos
```bash
docker compose down -v
docker compose up --build -d
```
