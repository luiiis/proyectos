# Proyecto Final: ERP Empresarial

## ¿Qué construimos?
Sistema de gestión empresarial completo que integra TODOS los proyectos anteriores.

## Stack
- **Frontend**: Angular 17+ con PrimeNG
- **Backend**: Spring Boot 3.3 + Spring Security + JWT
- **Base de Datos**: PostgreSQL 16
- **Caché**: Redis 7
- **Mensajería**: Apache Kafka
- **Contenedores**: Docker + Docker Compose
- **Observabilidad**: Prometheus + Grafana

## Módulos
- Usuarios y Roles (autenticación JWT)
- Clientes (CRUD + búsqueda)
- Productos (catálogo + categorías)
- Inventario (stock por sucursal + alertas)
- Ventas (registro + detalle + facturación)
- Compras (proveedores + recepción)
- Auditoría (log de todas las acciones)
- Reportes (ventas por período, top productos, dashboard)
- Notificaciones (eventos Kafka: stock bajo, venta grande)

## Arquitectura
```
Angular (4200) → Nginx (80) → Spring Boot (8080) → PostgreSQL (5432)
                                    ↓                      ↑
                               Redis (6379)          Kafka (9092)
                                    ↓
                          Prometheus (9090) → Grafana (3000)
```
