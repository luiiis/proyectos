# Módulo 21: Proyecto Final - Sistema Empresarial Completo

## Objetivo
Construir un sistema de gestión empresarial fullstack que integre TODO lo aprendido en los 20 módulos anteriores.

---

## Arquitectura del Proyecto Final

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (Angular)                         │
│  ┌────────┐ ┌──────────┐ ┌──────────┐ ┌─────────────────┐  │
│  │ Login  │ │Dashboard │ │Productos │ │Ventas/Reportes  │  │
│  └────────┘ └──────────┘ └──────────┘ └─────────────────┘  │
└──────────────────────────┬──────────────────────────────────┘
                           │ REST API + JWT
┌──────────────────────────┼──────────────────────────────────┐
│              BACKEND (Spring Boot)                            │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ Security │ Controllers │ Services │ Repositories     │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ Auditoría │ Caché Redis │ Validaciones │ Reportes    │   │
│  └──────────────────────────────────────────────────────┘   │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────┼──────────────────────────────────┐
│  ┌──────────┐    ┌───────┐    ┌──────────────────────────┐  │
│  │PostgreSQL│    │ Redis │    │ Keycloak (Auth)          │  │
│  │ empresa  │    │ cache │    │ login, roles, MFA        │  │
│  └──────────┘    └───────┘    └──────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## Funcionalidades Requeridas

### 1. Autenticación y Autorización
- [ ] Login con JWT (via Keycloak o custom)
- [ ] Roles: ADMIN, GERENTE, VENDEDOR, ALMACENISTA, AUDITOR
- [ ] Permisos granulares por módulo
- [ ] Refresh token
- [ ] Logout (invalidar token)

### 2. Módulo de Usuarios
- [ ] CRUD de usuarios
- [ ] Asignar/remover roles
- [ ] Activar/desactivar usuarios
- [ ] Historial de accesos

### 3. Módulo de Productos
- [ ] CRUD completo con validaciones
- [ ] Búsqueda por nombre, categoría, rango de precio
- [ ] Paginación y ordenamiento
- [ ] Imágenes de producto (URL)
- [ ] Categorías y proveedores

### 4. Módulo de Inventario
- [ ] Stock por sucursal
- [ ] Entradas y salidas con motivo
- [ ] Alertas de stock bajo
- [ ] Transferencias entre sucursales
- [ ] Historial de movimientos

### 5. Módulo de Ventas
- [ ] Registrar venta (encabezado + detalle)
- [ ] Calcular subtotal, IVA, total automáticamente
- [ ] Descontar inventario al vender
- [ ] Cancelar/devolver venta (restaurar inventario)
- [ ] Métodos de pago

### 6. Módulo de Compras
- [ ] Registrar compra a proveedor
- [ ] Actualizar inventario al recibir
- [ ] Historial de compras por proveedor

### 7. Módulo de Reportes
- [ ] Ventas por período (día, semana, mes, año)
- [ ] Top productos más vendidos
- [ ] Top clientes por facturación
- [ ] Ranking de vendedores
- [ ] Inventario valorizado
- [ ] Margen de ganancia por producto
- [ ] Exportar a CSV/PDF

### 8. Dashboard
- [ ] KPIs: ventas del día, mes, año
- [ ] Gráficas de tendencia
- [ ] Alertas activas (stock bajo, ventas pendientes)
- [ ] Últimas ventas en tiempo real

### 9. Auditoría
- [ ] Log de TODAS las operaciones (quién, qué, cuándo)
- [ ] Datos antes/después de cada cambio
- [ ] Filtrar por usuario, tabla, fecha
- [ ] Implementado con Triggers en BD

### 10. Características Técnicas
- [ ] Caché Redis para consultas frecuentes
- [ ] Rate limiting en endpoints
- [ ] Paginación en todas las listas
- [ ] Validación en frontend Y backend
- [ ] Manejo global de errores
- [ ] Documentación Swagger/OpenAPI
- [ ] Tests unitarios y de integración
- [ ] Docker Compose para todo el stack
- [ ] CI/CD con GitHub Actions

---

## Estructura de Carpetas del Proyecto Final

```
21-proyecto-final/
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│       ├── main/java/com/empresa/
│       │   ├── config/
│       │   ├── domain/entity/
│       │   ├── domain/repository/
│       │   ├── domain/service/
│       │   ├── api/controller/
│       │   ├── api/dto/
│       │   └── exception/
│       └── main/resources/
│           ├── application.yml
│           └── db/migration/
├── frontend/
│   ├── package.json
│   ├── Dockerfile
│   └── src/app/
│       ├── core/
│       ├── features/
│       │   ├── auth/
│       │   ├── dashboard/
│       │   ├── productos/
│       │   ├── ventas/
│       │   ├── inventario/
│       │   └── reportes/
│       └── shared/
├── docker-compose.yml
└── docs/
    ├── API.md
    ├── ARQUITECTURA.md
    └── DESPLIEGUE.md
```

---

## Evaluación del Proyecto Final

### Criterios de Evaluación

| Criterio | Peso | Descripción |
|----------|------|-------------|
| Funcionalidad | 30% | Todas las features funcionan correctamente |
| Código limpio | 20% | Nombres claros, sin duplicación, bien organizado |
| Seguridad | 15% | Auth, validaciones, SQL injection prevention |
| Performance | 15% | Índices, caché, paginación, queries optimizadas |
| Testing | 10% | Tests unitarios + integración con cobertura > 70% |
| Documentación | 10% | Swagger, README, comentarios en código complejo |

### Retos Adicionales (Nivel Senior)

1. **Implementar CQRS**: Separar modelo de lectura (optimizado) del de escritura
2. **Event Sourcing**: Registrar eventos en lugar de estado actual
3. **Multi-tenancy**: Que el sistema soporte múltiples empresas aisladas
4. **Replicación**: Configurar read replicas para reportes
5. **Búsqueda avanzada**: Implementar Elasticsearch para búsqueda full-text
6. **Notificaciones real-time**: WebSockets para alertas de stock
7. **API versioning**: Soportar v1 y v2 simultáneamente
8. **Rate limiting distribuido**: Con Redis (no solo en memoria)

---

## Entregables

1. Código fuente en repositorio Git
2. Docker Compose que levante TODO con un comando
3. Documentación de API (Swagger)
4. README con instrucciones de instalación
5. Colección de Postman/Insomnia con todos los endpoints
6. Diagrama ER actualizado
7. Al menos 50 tests pasando
8. Demo funcional (video o deploy)

---

## ¡Felicidades!
Si completaste los 21 módulos, tienes el conocimiento de un Backend Developer + DBA Junior-Mid.
Para llegar a Senior, necesitas: experiencia en producción, debugging bajo presión, y diseño de sistemas a escala.
