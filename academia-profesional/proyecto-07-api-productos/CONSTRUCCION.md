# Bitácora de Construcción - Proyecto 07: API Productos

## ¿Qué cambió respecto al Proyecto 06?
El proyecto 06 almacenaba datos en memoria (HashMap). Este proyecto conecta a PostgreSQL real con JPA.

## Decisiones técnicas

| Decisión | Razón |
|----------|-------|
| PostgreSQL | BD más avanzada, JSONB, extensiones, gratis |
| Spring Data JPA | Genera queries automáticamente por nombre de método |
| Pageable | Paginación estándar sin código manual |
| Docker Compose | BD reproducible en cualquier máquina |
| ddl-auto: update | Solo para desarrollo (en producción usar Flyway) |

## Evolución: Proyecto 06 → 07

```
Proyecto 06:                    Proyecto 07:
Map<Long, Cliente> en memoria → ProductoRepository (JPA → PostgreSQL)
Sin persistencia              → Datos sobreviven reinicios
Sin paginación                → Page<T> con sort automático
Sin Docker                    → docker-compose.yml incluido
```

## Cómo se conecta Java con PostgreSQL

```
Controller → Service → Repository (interface)
                            ↓
                    Spring Data JPA genera implementación
                            ↓
                    Hibernate genera SQL
                            ↓
                    JDBC Driver envía al servidor PostgreSQL
                            ↓
                    PostgreSQL ejecuta y devuelve resultados
```

## Endpoints disponibles
```
GET    /api/productos              → Listar (paginado: ?page=0&size=20&sort=precio,desc)
GET    /api/productos/{id}         → Buscar por ID
GET    /api/productos/buscar?q=    → Buscar por nombre
GET    /api/productos/categorias   → Listar categorías
GET    /api/productos/categoria/X  → Filtrar por categoría
GET    /api/productos/stock-bajo   → Alertas de stock
POST   /api/productos              → Crear
PUT    /api/productos/{id}         → Actualizar
DELETE /api/productos/{id}         → Eliminar (soft delete)
```
