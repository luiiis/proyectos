# Cómo Ejecutar — Proyecto 12: ERP Depósitos

## Requisitos
- Java 21 + Maven
- MySQL 8
- Node.js 22 + Angular CLI 20
- Docker (para las versiones 7+)

## Crear BD
```sql
CREATE DATABASE erp_db;
```

## Backend
```cmd
cd nivel-12-erp/proyecto/backend
mvn spring-boot:run
```

## Frontend
```cmd
cd nivel-12-erp/proyecto/frontend
npm install
ng serve
```

## Docker (Versión 7+)
```cmd
cd nivel-12-erp/proyecto
docker-compose up -d
```

## Módulos

| Módulo | Endpoints |
|--------|-----------|
| Auth | /api/auth/* |
| Usuarios | /api/usuarios/* |
| Sucursales | /api/sucursales/* |
| Cajas | /api/cajas/* |
| Clientes | /api/clientes/* |
| Proveedores | /api/proveedores/* |
| Categorías | /api/categorias/* |
| Productos | /api/productos/* |
| Inventario | /api/inventario/* |
| Compras | /api/compras/* |
| Ventas | /api/ventas/* |
| Cortes de caja | /api/cortes/* |
| Gastos | /api/gastos/* |
| Reportes | /api/reportes/* |
| Configuración | /api/config/* |
| Auditoría | /api/auditoria/* |

## Versiones del proyecto

| V | Foco |
|---|------|
| 1 | Monolito básico funcional |
| 2 | Separar módulos internamente |
| 3 | Arquitectura hexagonal |
| 4 | Multiusuario con roles granulares |
| 5 | Multisucursal (stock por sucursal) |
| 6 | Multiempresa (tenant isolation) |
| 7 | Docker (contenedores) |
| 8 | CI/CD (GitHub Actions) |
| 9 | Monitoreo (Actuator + logs) |
| 10 | Producción (HTTPS, backups, dominio) |
