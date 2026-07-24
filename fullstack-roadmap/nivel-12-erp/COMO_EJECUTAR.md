# Cómo Ejecutar — Nivel 12: Depósitos ERP

## Prerrequisitos
- Docker Desktop (8GB+ RAM asignados)
- O: Java 21 + Maven + MySQL + Node.js + Angular CLI

## Ejecutar con Docker (recomendado)
```cmd
cd nivel-12-erp/proyecto
cp .env.example .env
docker compose up --build -d
# Esperar ~60 segundos

# Frontend: http://localhost
# Backend: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
# MySQL: localhost:3306
```

## Ejecutar en desarrollo local
```cmd
# BD
docker run --name erp-mysql -e MYSQL_ROOT_PASSWORD=Root123! -e MYSQL_DATABASE=erp_db -p 3306:3306 -d mysql:8.0

# Backend
cd backend && mvn spring-boot:run

# Frontend  
cd frontend && npm install && ng serve
```

## Módulos del ERP
| Módulo | Ruta | Descripción |
|--------|------|-------------|
| Auth | /login | Login + JWT + refresh |
| Dashboard | /dashboard | KPIs + gráficas |
| Usuarios | /admin/usuarios | CRUD + roles + permisos |
| Sucursales | /admin/sucursales | Gestión de puntos de venta |
| Categorías | /catalogo/categorias | Catálogo |
| Productos | /catalogo/productos | CRUD + imágenes |
| Inventario | /inventario | Stock por sucursal |
| Compras | /compras | Compra a proveedores → entrada |
| Ventas | /ventas | Punto de venta → ticket |
| Corte de caja | /caja/corte | Cierre diario |
| Reportes | /reportes | Ventas, inventario, financiero |
| Auditoría | /admin/auditoria | Log de acciones |

## Credenciales
| Usuario | Password | Rol |
|---------|----------|-----|
| admin | Admin123! | ADMIN |
| supervisor | Super123! | SUPERVISOR |
| cajero | Cajero123! | CAJERO |
| almacenista | Almacen123! | ALMACENISTA |

## Versiones del proyecto
Este ERP se desarrolla en 10 versiones progresivas (ver README.md para detalle).
Empieza con V1 (monolito básico) y escala hasta V10 (producción con HTTPS + monitoreo).
