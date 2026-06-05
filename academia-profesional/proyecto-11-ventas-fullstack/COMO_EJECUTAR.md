# Cómo Ejecutar - Proyecto 11: Sistema Ventas Full Stack

## Arquitectura
```
Angular (4200) ──HTTP/JSON──→ Spring Boot (8080) ──JPA──→ PostgreSQL (5432)
```

## Levantar TODO

### Opción 1: Docker Compose (producción)
```bash
cd academia-profesional/proyecto-11-ventas-fullstack
docker compose up --build -d

# Frontend: http://localhost
# API: http://localhost:8080/api
# Swagger: http://localhost:8080/swagger-ui.html
```

### Opción 2: Desarrollo local
```bash
# Terminal 1: Base de datos
docker compose up postgres -d

# Terminal 2: Backend
cd backend
mvn spring-boot:run
# → http://localhost:8080

# Terminal 3: Frontend
cd frontend
npm install
ng serve
# → http://localhost:4200
```

## Flujo de uso
1. Abrir http://localhost:4200
2. Login: admin / admin123
3. Dashboard muestra KPIs
4. Navegar a Productos → CRUD completo
5. Navegar a Ventas → Registrar nueva venta
6. Navegar a Reportes → Ver ventas por período

## Credenciales
| Usuario | Password | Rol |
|---------|----------|-----|
| admin | admin123 | ADMIN |
| vendedor | vendedor123 | VENDEDOR |
