# Cómo Ejecutar — Nivel 11: Sistema de Inventario

## Prerrequisitos
- Java 21 + Maven + MySQL + Node.js + Angular CLI
- Docker (opcional, para levantar BD)

## Opción 1: Docker Compose (todo junto)
```cmd
cd nivel-11-inventario/proyecto
docker compose up --build -d
# Frontend: http://localhost
# Backend: http://localhost:8080
# Login: admin / Admin123!
```

## Opción 2: Desarrollo local
```cmd
# 1. BD
docker run --name inv-mysql -e MYSQL_ROOT_PASSWORD=Root123! -e MYSQL_DATABASE=inventario_db -p 3306:3306 -d mysql:8.0

# 2. Backend
cd backend && mvn spring-boot:run

# 3. Frontend
cd frontend && npm install && ng serve
```

## Módulos incluidos
- /login → Autenticación JWT
- /dashboard → KPIs (total productos, valor inventario, alertas)
- /categorias → CRUD de categorías
- /productos → CRUD + búsqueda + filtros + paginación
- /proveedores → CRUD de proveedores
- /compras → Registrar compras (entrada de mercancía → suma stock)
- /movimientos → Historial de entradas/salidas
- /reportes → Productos más vendidos, stock bajo, valor por categoría

## Usuarios de prueba
| Usuario | Password | Rol | Puede hacer |
|---------|----------|-----|-------------|
| admin | Admin123! | ADMIN | Todo |
| supervisor | Super123! | SUPERVISOR | CRUD + reportes |
| almacenista | Almacen123! | ALMACENISTA | Productos + movimientos |
