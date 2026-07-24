# Cómo Ejecutar — Proyecto 11: Sistema de Inventario

## Requisitos
- Java 21 + Maven 3.9+
- MySQL 8 corriendo
- Node.js 22 + Angular CLI 20

## Crear BD
```sql
CREATE DATABASE inventario_db;
```

## Backend
```cmd
cd nivel-11-inventario/proyecto/backend
mvn spring-boot:run
```

## Frontend
```cmd
cd nivel-11-inventario/proyecto/frontend
npm install
ng serve
```
→ http://localhost:4200

## Módulos del sistema

| Módulo | Endpoints | Descripción |
|--------|-----------|-------------|
| Auth | /api/auth/* | Login, refresh, registro |
| Usuarios | /api/usuarios/* | CRUD + roles + permisos |
| Categorías | /api/categorias/* | CRUD |
| Proveedores | /api/proveedores/* | CRUD |
| Productos | /api/productos/* | CRUD + búsqueda + paginación |
| Movimientos | /api/movimientos/* | Entradas, salidas, ajustes |
| Compras | /api/compras/* | Cabecera + detalle + recibir |
| Reportes | /api/reportes/* | Existencias, movimientos, alertas |

## Usuarios de prueba

| Usuario | Rol | Acceso |
|---------|-----|--------|
| admin | ADMIN | Todo |
| almacen | ALMACENISTA | Productos, compras, movimientos |
| consulta | CONSULTOR | Solo lectura |
